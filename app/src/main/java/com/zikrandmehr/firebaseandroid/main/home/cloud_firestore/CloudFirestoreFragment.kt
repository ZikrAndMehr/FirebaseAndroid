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

package com.zikrandmehr.firebaseandroid.main.home.cloud_firestore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.R
import com.zikrandmehr.firebaseandroid.databinding.FragmentCloudFirestoreBinding
import com.zikrandmehr.firebaseandroid.utils.showErrorAlert

class CloudFirestoreFragment : Fragment() {

    private var _binding: FragmentCloudFirestoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var quoteRef: DocumentReference
    private lateinit var quoteListener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloudFirestoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteRef = Firebase.firestore
            .collection(CFConstants.COLLECTION_GLOBAL)
            .document(CFConstants.DOCUMENT_QUOTE)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        addQuoteListener()
    }

    override fun onStop() {
        super.onStop()
        removeQuoteListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addQuoteListener() {
        quoteListener = quoteRef.addSnapshotListener { value, _ ->
            value?.data?.let { quoteData ->
                binding.tvQuote.text = quoteData[CFConstants.KEY_QUOTE_TEXT] as String
                binding.tvAuthor.text = quoteData[CFConstants.KEY_QUOTE_AUTHOR] as String
            }
        }
    }

    private fun removeQuoteListener() {
        quoteListener.remove()
    }

    private fun setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            btnUpdate.setOnClickListener { updateQuote() }
        }
    }

    private fun updateQuote() {
        val quote = binding.etQuote.text.toString()
        val author = binding.etAuthor.text.toString()
        if (quote.isBlank() || author.isBlank()) return

        val quoteData = mapOf(
            CFConstants.KEY_QUOTE_TEXT to quote,
            CFConstants.KEY_QUOTE_AUTHOR to author
        )

        quoteRef.set(quoteData)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    showErrorAlert(getText(R.string.update_quote_error))
                }
            }
    }
}