package com.vm.vishunotesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vm.vishunotesapp.databinding.NoteItemBinding
import com.vm.vishunotesapp.models.NoteResponse

class NotesAdapter(private val onNoteClicked: (NoteResponse) -> Unit): ListAdapter<NoteResponse, NotesAdapter.NoteViewHolder>(NoteDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteItem = getItem(position)
        noteItem?.let {
            holder.bind(it)
        }
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        // Bind the note data to the view
        fun bind(note: NoteResponse) {
            binding.title.text = note.title
            binding.description.text = note.description
            binding.root.setOnClickListener {
                // Call the onNoteClicked lambda function
                onNoteClicked(note)
            }
        }
    }

    /**
     * [DiffUtil] implementation used to calculate the difference between two lists.
     * This is used by the [ListAdapter] to determine what has changed between the lists, and
     * only re-render the items that have changed.
     */
    class NoteDiffUtil: DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }
    }
}




