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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zikrandmehr.firebaseandroid.databinding.NoteViewHolderBinding

class NoteItem (
    val noteId: String,
    val uid: String,
    val username: String,
    val title: String,
    val description: String
)

class RealtimeDatabaseAdapter(
    private val onDeleteButtonClicked: (String) -> Unit
) : RecyclerView.Adapter<RealtimeDatabaseAdapter.NoteViewHolder>() {

    private var items: List<NoteItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteViewHolderBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun updateItems(items: List<NoteItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(
        private val binding: NoteViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteItem) {
            binding.apply {
                tvUsername.text = item.username
                tvTitle.text = item.title
                tvDescription.text = item.description
                ibDeleteNote.setOnClickListener { onDeleteButtonClicked(item.noteId) }
            }
        }
    }
}