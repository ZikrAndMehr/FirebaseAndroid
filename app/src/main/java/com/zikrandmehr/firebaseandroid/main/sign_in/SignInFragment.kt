package com.zikrandmehr.firebaseandroid.main.sign_in

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
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

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var signInClient: SignInClient
    private lateinit var auth: FirebaseAuth

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleSignInWithGoogleResult(result.data)
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

        signInClient = Identity.getSignInClient(requireContext())
        auth = Firebase.auth

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            etEmail.setText("android01@new.com")
            etPassword.setText("Welcome1.")
            btnSignInWithGoogle.setOnClickListener { signInWithGoogle() }
            btnSignIn.setOnClickListener { signIn() }
        }
    }

    private fun signInWithGoogle() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
            Toast.makeText(requireContext(), "ONE_TAP", Toast.LENGTH_SHORT).show()
        } else {
            val signInRequest = GetSignInIntentRequest.builder()
                .setServerClientId(getString(R.string.default_web_client_id))
                .build()

            signInClient.getSignInIntent(signInRequest)
                .addOnSuccessListener { pendingIntent ->
                    launchSignInWithGoogle(pendingIntent)
                }
        }
    }

    private fun oneTapSignIn() {
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build(),
            )
            .build()

        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignInWithGoogle(result.pendingIntent)
            }
            .addOnFailureListener {
                Log.e("ONE_TAP_ERROR", it.toString())
            }
    }

    private fun launchSignInWithGoogle(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent).build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            showSignInWithGoogleErrorAlert()
        }
    }

    private fun handleSignInWithGoogleResult(data: Intent?) {
        showProgressBar(true)
        try {
            val credential = signInClient.getSignInCredentialFromIntent(data)
            credential.googleIdToken?.let {
                firebaseAuthWithGoogle(it)
            }
        } catch (e: ApiException) {
            showSignInWithGoogleErrorAlert()
            showProgressBar(false)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                showProgressBar(false)
                if (task.isSuccessful) navigateToHomeFragment()
                else showSignInWithGoogleErrorAlert()
            }
    }

    private fun signIn() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isBlank() || password.isBlank()) return

        showProgressBar(true)

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                showProgressBar(false)
                if (it.isSuccessful) navigateToHomeFragment()
                else showSignInErrorAlert()
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

    private fun showSignInWithGoogleErrorAlert() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getText(R.string.sign_in_with_google_error))
        builder.setPositiveButton(getText(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showSignInErrorAlert() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getText(R.string.sign_in_error))
        builder.setPositiveButton(getText(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}