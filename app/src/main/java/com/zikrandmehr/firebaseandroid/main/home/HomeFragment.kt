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

package com.zikrandmehr.firebaseandroid.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentHomeBinding
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor()
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setStatusBarColor() {
        val window = requireActivity().window
        val windowColor = ContextCompat.getColor(requireContext(), R.color.firebase_orange)
        window.statusBarColor = windowColor
        window.navigationBarColor = windowColor
    }

    private fun setupViews() {
        val adapter = HomeAdapter(getHomeItems())
        binding.rvHome.adapter = adapter
    }

    private fun getHomeItems() = listOf(
        HomeItem(
            imageView = R.drawable.fb_authentication,
            title = getText(R.string.auth_title),
            description = getText(R.string.auth_desc),
            onClick = { navigateToAuthenticationFragment() }
        ),
        HomeItem(
            imageView = R.drawable.fb_realtime_database,
            title = getText(R.string.realtime_database_title),
            description = getText(R.string.realtime_database_desc),
            onClick = { navigateToRealtimeDatabaseFragment() }
        ),
        HomeItem(
            imageView = R.drawable.fb_cloud_firestore,
            title = getText(R.string.cloud_firestore_title),
            description = getText(R.string.cloud_firestore_desc),
            onClick = { navigateToCloudFirestoreFragment() }
        ),
        HomeItem(
            imageView = R.drawable.fb_storage,
            title = getText(R.string.storage_title),
            description = getText(R.string.storage_desc),
            onClick = { navigateToStorageFragment() }
        ),
        HomeItem(
            imageView = R.drawable.fb_messaging,
            title = getText(R.string.messaging_title),
            description = getText(R.string.messaging_desc),
            onClick = { Toast.makeText(requireContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show() }
        ),
        HomeItem(
            imageView = R.drawable.fb_crashlytics,
            title = getText(R.string.crashlytics_title),
            description = getText(R.string.crashlytics_desc),
            onClick = { navigateToCrashlyticsFragment() }
        ),
        HomeItem(
            imageView = R.drawable.fb_remote_config,
            title = getText(R.string.remote_config_title),
            description = getText(R.string.remote_config_desc),
            onClick = { Toast.makeText(requireContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show() }
        )
    )

    private fun navigateToAuthenticationFragment() {
        val directions = HomeFragmentDirections.actionHomeFragmentToAuthenticationFragment()
        findNavController().navigateWithDefaultAnimation(directions)
        resetStatusBarColor()
    }

    private fun navigateToRealtimeDatabaseFragment() {
        val directions = HomeFragmentDirections.actionHomeFragmentToRealtimeDatabaseFragment()
        findNavController().navigateWithDefaultAnimation(directions)
        resetStatusBarColor()
    }

    private fun navigateToCloudFirestoreFragment() {
        val directions = HomeFragmentDirections.actionHomeFragmentToCloudFirestoreFragment()
        findNavController().navigateWithDefaultAnimation(directions)
        resetStatusBarColor()
    }

    private fun navigateToStorageFragment() {
        val directions = HomeFragmentDirections.actionHomeFragmentToStorageFragment()
        findNavController().navigateWithDefaultAnimation(directions)
        resetStatusBarColor()
    }

    private fun navigateToCrashlyticsFragment() {
        val directions = HomeFragmentDirections.actionHomeFragmentToCrashlyticsFragment()
        findNavController().navigateWithDefaultAnimation(directions)
        resetStatusBarColor()
    }

    private fun resetStatusBarColor() {
        val window = requireActivity().window
        val defaultStatusBarColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        val defaultNavigationBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)

        window.statusBarColor = defaultStatusBarColor
        window.navigationBarColor = defaultNavigationBarColor
    }
}