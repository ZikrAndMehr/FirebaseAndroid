package com.zikrandmehr.firebaseandroid.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        binding.apply {
            btnSignIn.setOnClickListener { navigateToSignInFragment() }
            btnSignUp.setOnClickListener { navigateToSignUpFragment() }
        }
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
