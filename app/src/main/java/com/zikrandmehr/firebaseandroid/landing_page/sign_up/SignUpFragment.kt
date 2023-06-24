package com.zikrandmehr.firebaseandroid.landing_page.sign_up

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
import com.zikrandmehr.firebaseandroid.databinding.FragmentSignUpBinding
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation

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

    private fun setupViews() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            btnSignUp.setOnClickListener { signUp() }
        }
    }

    private fun signUp() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isBlank() || password.isBlank()) return

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) navigateToHomeFragment()
                else showSignUpErrorAlert()
            }
    }

    private fun navigateToHomeFragment() {
        val directions = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
        findNavController().navigateWithDefaultAnimation(directions)
    }

    private fun showSignUpErrorAlert() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getText(R.string.sign_up_error))
        builder.setPositiveButton(getText(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}