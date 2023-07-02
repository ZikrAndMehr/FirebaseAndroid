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