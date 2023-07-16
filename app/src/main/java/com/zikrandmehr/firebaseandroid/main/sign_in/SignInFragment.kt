package com.zikrandmehr.firebaseandroid.main.sign_in

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentSignInBinding
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation
import com.zikrandmehr.firebaseandroid.utils.showErrorAlert

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var oneTapClient: SignInClient
    private lateinit var auth: FirebaseAuth

    private val oneTapLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleOneTapWithGoogleResult(result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTapClient = Identity.getSignInClient(requireContext())
        auth = Firebase.auth

        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViews() {
        //TODO should be rechecked
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            etEmail.setText("android01@new.com")
            etPassword.setText("Welcome1.")
            btnSignInWithGoogle.setOnClickListener { signInWithGoogle() }
            btnSignIn.setOnClickListener { signIn() }
        }
    }

    private fun signInWithGoogle() {
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build(),
            )
            .build()

        oneTapClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignInWithGoogle(result.pendingIntent)
            }
            .addOnFailureListener {
                //TODO
                signUpWithGoogle()
                it.message?.let { it1 -> showErrorAlert(it1) }
            }
    }

    private fun signUpWithGoogle() {
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build(),
            )
            .build()

        oneTapClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignInWithGoogle(result.pendingIntent)
            }
    }

    private fun launchSignInWithGoogle(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent).build()
            oneTapLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            e.message?.let { showErrorAlert(it) }
        }
    }

    private fun handleOneTapWithGoogleResult(data: Intent?) {
        showProgressBar(true)
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            credential.googleIdToken?.let {
                firebaseAuthWithGoogle(it)
            }
        } catch (e: ApiException) {
            e.message?.let { showErrorAlert(it) }
            showProgressBar(false)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                showProgressBar(false)
                if (task.isSuccessful) navigateToHomeFragment()
                else showErrorAlert(getText(R.string.sign_in_with_google_error))
            }
    }

    private fun signIn() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isBlank() || password.isBlank()) return

        showProgressBar(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                showProgressBar(false)
                if (it.isSuccessful) navigateToHomeFragment()
                else showErrorAlert(getText(R.string.sign_in_error))
            }
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToHomeFragment() {
        val directions = SignInFragmentDirections.actionSignInFragmentToHomeNav()
        findNavController().navigateWithDefaultAnimation(
            directions = directions,
            popUpToDestinationId = R.id.landingPageFragment
        )
    }
}