/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
