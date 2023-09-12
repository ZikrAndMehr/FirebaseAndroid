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

package com.zikrandmehr.firebaseandroid.main.home.realtime_database

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zikrandmehr.firebaseandroid.databinding.FragmentRealtimeDatabaseBinding
import com.zikrandmehr.firebaseandroid.utils.navigateWithDefaultAnimation

class RealtimeDatabaseFragment : Fragment() {

    private var _binding: FragmentRealtimeDatabaseBinding? = null
    private val binding  get() = _binding!!

    private var user: FirebaseUser? = null
    private lateinit var ref: DatabaseReference
    private lateinit var noteAdapter: RealtimeDatabaseAdapter
    private lateinit var noteListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRealtimeDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = Firebase.auth.currentUser
        ref = Firebase.database.reference
        noteAdapter = RealtimeDatabaseAdapter { deleteNote(it) }

        checkUserData()
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        fetchUserNotes()
    }

    override fun onStop() {
        super.onStop()
        val userUid = user?.uid
        userUid?.let { uid ->
            ref.child(RDConstants.NODE_USER_NOTES).child(uid)
                .removeEventListener(noteListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViews() {
        binding.apply {
            toolbar.root.setNavigationOnClickListener { findNavController().navigateUp() }
            rvNote.adapter = noteAdapter
            btnAddNewNote.setOnClickListener { navigateToNewNoteFragment() }
        }
    }

    private fun checkUserData() {
        val userUid = user?.uid

        userUid?.let { uid ->
            ref.child(RDConstants.NODE_USERS).child(uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            val userData = mapOf(
                                RDConstants.KEY_EMAIL to (user?.email ?: ""),
                                RDConstants.KEY_USERNAME to usernameFromEmail(user?.email ?: "")
                            )
                            ref.child(RDConstants.NODE_USERS).child(uid).setValue(userData)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        }
    }

    private fun fetchUserNotes() {
        val userUid = user?.uid

        userUid?.let {
            val userNotesRef = ref.child(RDConstants.NODE_USER_NOTES).child(it)

            noteListener = userNotesRef.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val notesData = snapshot.value as? Map<String, Map<String, Any>> ?: return

                        val updatedNotes: MutableList<NoteItem> = mutableListOf()

                        for ((noteId, noteData) in notesData) {
                            val uid = noteData[RDConstants.KEY_UID] as String
                            val username = noteData[RDConstants.KEY_USERNAME] as String
                            val title = noteData[RDConstants.KEY_TITLE] as String
                            val description = noteData[RDConstants.KEY_DESCRIPTION] as String

                            val note = NoteItem(noteId, uid, username, title, description)
                            updatedNotes.add(note)
                        }
                        noteAdapter.updateItems(updatedNotes)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        }
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            val components = email.split("@")
            components.firstOrNull() ?: ""
        } else {
            email
        }
    }

    private fun deleteNote(noteId: String) {
        val userUid = user?.uid
        userUid?.let { uid ->
            ref.child(RDConstants.NODE_USER_NOTES).child(uid).child(noteId)
                .removeValue { error, ref ->
                    if (error != null) {
                        //handle error if necessary
                    }
                }
        }
    }

    private fun navigateToNewNoteFragment() {
        val directions = RealtimeDatabaseFragmentDirections.actionRealtimeDatabaseFragmentToNewNoteFragment()
        findNavController().navigateWithDefaultAnimation(directions)
    }
}