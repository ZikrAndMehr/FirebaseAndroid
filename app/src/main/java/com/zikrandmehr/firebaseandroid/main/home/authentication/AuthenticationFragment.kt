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
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation
import com.zikrandmehr.firebaseandroid.utils.showErrorAlert

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
        //TODO
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
                else showErrorAlert(getText(R.string.sign_out_error))
            }
        }
    }

    private fun navigateToLandingPageFragment() {
        if (isAdded) {
            findNavController().navigateWithDefaultAnimation(
                resId = R.id.landingPageFragment,
                popUpToDestinationId = R.id.home_nav
            )
        }
    }
}