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

package com.zikrandmehr.firebaseandroid.main.home.realtime_database.new_note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.databinding.FragmentNewNoteBinding
import com.zikrandmehr.firebaseandroid.main.home.realtime_database.RDConstants

class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = Firebase.database.reference

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            btnSave.setOnClickListener { saveNote() }
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        if (title.isBlank() || description.isBlank()) return

        val uid = Firebase.auth.currentUser?.uid

        uid?.let { userUid ->
            val userRef = ref.child(RDConstants.NODE_USERS).child(userUid)

            userRef.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.value as? Map<String, Any> ?: return
                        val username = userData[RDConstants.KEY_USERNAME] as String

                        writeNewNote(userUid, username, title, description)
                    }
                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        }
    }

    private fun writeNewNote(
        uid: String,
        username: String,
        title: String,
        description: String
    ) {
        val userNotesRef = ref.child(RDConstants.NODE_USER_NOTES).child(uid)
        val key = userNotesRef.push().key ?: return

        val newNote = mapOf(
            RDConstants.KEY_UID to uid,
            RDConstants.KEY_USERNAME to username,
            RDConstants.KEY_TITLE to title,
            RDConstants.KEY_DESCRIPTION to description
        )

        userNotesRef.child(key).setValue(newNote).addOnCompleteListener {
            if (it.isSuccessful) {
                findNavController().navigateUp()
            }
        }
    }
}