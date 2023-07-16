package com.zikrandmehr.firebaseandroid.main.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentSignUpBinding
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation
import com.zikrandmehr.firebaseandroid.utils.showErrorAlert

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            btnSignUp.setOnClickListener { signUp() }
        }
    }

    private fun signUp() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isBlank() || password.isBlank()) return

        showProgressBar(true)

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                showProgressBar(false)
                if (it.isSuccessful) {
                    sendVerificationEmail()
                    navigateToHomeFragment()
                } else {
                    showErrorAlert(getText(R.string.sign_up_error))
                }
            }
    }

    private fun sendVerificationEmail() {
        val user = Firebase.auth.currentUser

        user?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        //handle error if necessary
                    }
                }
        }
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToHomeFragment() {
        val directions = SignUpFragmentDirections.actionSignUpFragmentToHomeNav()
        findNavController().navigateWithDefaultAnimation(
            directions = directions,
            popUpToDestinationId = R.id.landingPageFragment
        )
    }
}