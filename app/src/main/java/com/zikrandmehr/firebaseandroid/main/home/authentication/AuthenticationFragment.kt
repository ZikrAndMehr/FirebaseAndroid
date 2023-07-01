package com.zikrandmehr.firebaseandroid.main.home.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentAuthenticationBinding
import com.zikrandmehr.firebaseandroid.utils.AppConstants

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var authStateListener: AuthStateListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        user = auth.currentUser

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenAuthState()
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listenAuthState() {
       auth.addAuthStateListener {
           if (it.currentUser == null) {
               navigateToLandingPageFragment()
               it.removeAuthStateListener {  }
           }
       }
    }

    private fun setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            user?.let {
                tvUid.text = it.uid
                tvEmail.text = it.email
                tvAnonymous.text = if (it.isAnonymous) getText(R.string.yes) else getText(R.string.no)
                tvEmailVerified.text = if (it.isEmailVerified) getText(R.string.yes) else getText(R.string.no)
            }
            btnSignOut.setOnClickListener { signOut() }
            btnDeleteAccount.setOnClickListener { deleteAccount() }
        }
    }

    private fun signOut() {
        auth.signOut()

        if (isGoogleProvider()) Identity.getSignInClient(requireContext()).signOut()

        navigateToLandingPageFragment()
    }

    private fun isGoogleProvider(): Boolean {
        var providerId: String? = null
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                providerId = profile.providerId
            }
        }
        return providerId == AppConstants.PROVIDER_GOOGLE
    }

    private fun deleteAccount() {
        user?.let {
            it.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) navigateToLandingPageFragment()
                else showSignOutOrDeleteAccountErrorAlert()
            }
        }
    }

    private fun navigateToLandingPageFragment() {
        //TODO navigation should be implemented
    }

    private fun showSignOutOrDeleteAccountErrorAlert() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getText(R.string.sign_in_error))
        builder.setPositiveButton(getText(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}