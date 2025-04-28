package com.vm.vishunotesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.vm.vishunotesapp.databinding.FragmentNoteBinding
import com.vm.vishunotesapp.models.NoteRequest
import com.vm.vishunotesapp.models.NoteResponse
import com.vm.vishunotesapp.ui.viewmodel.NotesViewModel
import com.vm.vishunotesapp.utils.Constants.ARGS_NOTE
import com.vm.vishunotesapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [NoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private var note: NoteResponse? = null
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val noteResponse = it.getString(ARGS_NOTE)
            note = noteResponse?.let { Gson().fromJson(it, NoteResponse::class.java) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        note?.let {
            binding.addEditText.text = "Edit Note"
            binding.txtTitle.setText(it.title)
            binding.txtDescription.setText(it.description)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDeleteBtn()
        setupSubmitBtn()
    }

    private fun setupSubmitBtn() {
        binding.btnSubmit.setOnClickListener {
            if (note  == null) {
                createNote()
            } else {
                updateNote()
            }
        }
    }

    private fun createNote() {
        val noteRequest = NoteRequest(
            title = binding.txtTitle.text.toString(),
            description = binding.txtDescription.text.toString()
        )
        lifecycleScope.launch(Dispatchers.IO) {
            val result = notesViewModel.createNote(noteRequest)
            when (result) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main) {
                        findNavController().popBackStack()
                        Toast.makeText(context, "Note created successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {  }
            }
        }
    }

    private fun updateNote() {
        val noteRequest = NoteRequest(
            title = binding.txtTitle.text.toString(),
            description = binding.txtDescription.text.toString()
        )
        lifecycleScope.launch(Dispatchers.IO) {
            val result = notesViewModel.updateNote(note!!.id, noteRequest)
            when (result) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main) {
                        findNavController().popBackStack()
                        Toast.makeText(context, "Note updated successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {  }
            }
        }
    }

    private fun setupDeleteBtn() {
        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                var networkResult: NetworkResult<NoteResponse>? = if (note != null) {
                    notesViewModel.deleteNote(note!!.id)
                } else null

                withContext(Dispatchers.Main) {
                    if (networkResult == null || networkResult is NetworkResult.Success) {
                        findNavController().popBackStack()
                        Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else if (networkResult is NetworkResult.Error) {
                        Toast.makeText(context, networkResult.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}