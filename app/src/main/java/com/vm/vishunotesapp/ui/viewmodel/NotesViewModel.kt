package com.vm.vishunotesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vm.vishunotesapp.models.NoteRequest
import com.vm.vishunotesapp.models.NoteResponse
import com.vm.vishunotesapp.repository.NoteRepository
import com.vm.vishunotesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>> = noteRepository.notesLiveData

    fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    suspend fun createNote(noteRequest: NoteRequest): NetworkResult<NoteResponse> {
        return withContext(Dispatchers.IO) {
            noteRepository.createNote(noteRequest)
        }
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest): NetworkResult<NoteResponse> {
        return withContext(Dispatchers.IO) {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }

    suspend fun deleteNote(noteId: String): NetworkResult<NoteResponse> {
        return withContext(Dispatchers.IO) {
            noteRepository.deleteNote(noteId)
        }
    }
}