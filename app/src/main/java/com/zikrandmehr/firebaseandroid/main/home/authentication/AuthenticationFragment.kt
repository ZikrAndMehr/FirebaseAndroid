package com.zikrandmehr.firebaseandroid.main.home.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        val user = Firebase.auth.currentUser

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
        Firebase.auth.signOut()
        Firebase.auth.addAuthStateListener { auth ->
            if (auth.currentUser == null) navigateToLandingPageFragment()
            else showSignOutOrDeleteAccountErrorAlert()
        }
    }

    private fun deleteAccount() {
        val user = Firebase.auth.currentUser!!

        user.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) navigateToLandingPageFragment()
            else showSignOutOrDeleteAccountErrorAlert()
        }
    }

    private fun navigateToLandingPageFragment() {
        findNavController().popBackStack(R.id.landingPageFragment, false)
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