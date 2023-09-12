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

package com.zikrandmehr.firebaseandroid.main.home.crashlytics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentCrashlyticsBinding
import com.zikrandmehr.firebaseandroid.main.home.cloud_firestore.CFConstants
import io.grpc.Context.key
import java.lang.Exception

class CrashlyticsFragment : Fragment() {

    private var _binding: FragmentCrashlyticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrashlyticsBinding.inflate(inflater, container, false)
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
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            Firebase.crashlytics.setCustomKeys {
                key(CConstants.USER_UID, currentUser.uid)
                key(CConstants.USER_EMAIL, currentUser.email ?: "")
            }
        }
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            btnCrash1.setOnClickListener { makeCrash1() }
            btnCrash2.setOnClickListener { makeCrash2() }
            btnCrash3.setOnClickListener { makeCrash3() }
        }
    }

    private fun makeCrash1() {
        val numbers = arrayOf(0)
        numbers[1]
    }

    private fun makeCrash2() {
        val string = "one"
        string.toInt()
    }

    private fun makeCrash3() {
        throw RuntimeException("Test Crash") 
    }
}