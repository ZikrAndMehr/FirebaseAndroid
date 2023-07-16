package com.zikrandmehr.firebaseandroid.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentLandingPageBinding
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation

class LandingPageFragment : Fragment() {

    private var _binding: FragmentLandingPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkUser()) navigateToHomeFragment()
        else setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkUser(): Boolean {
        val user = Firebase.auth.currentUser
        return user != null
    }

    private fun setupViews() {
        binding.apply {
            btnSignIn.setOnClickListener { navigateToSignInFragment() }
            btnSignUp.setOnClickListener { navigateToSignUpFragment() }
        }
    }

    private fun navigateToHomeFragment() {
        val directions = LandingPageFragmentDirections.actionLandingPageFragmentToHomeNav()
        findNavController().navigateWithDefaultAnimation(
            directions = directions,
            popUpToDestinationId = R.id.landingPageFragment
        )
    }

    private fun navigateToSignInFragment() {
        val directions = LandingPageFragmentDirections.actionLandingPageFragmentToSignInFragment()
        findNavController().navigateWithDefaultAnimation(directions)
    }

    private fun navigateToSignUpFragment() {
        val directions = LandingPageFragmentDirections.actionLandingPageFragmentToSignUpFragment()
        findNavController().navigateWithDefaultAnimation(directions)
    }
}
