package com.vm.vishunotesapp.repository

import androidx.lifecycle.LiveData
import com.vm.vishunotesapp.models.NoteRequest
import com.vm.vishunotesapp.models.NoteResponse
import com.vm.vishunotesapp.utils.NetworkResult

interface NoteRepository {
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>>

    suspend fun getNotes()
    suspend fun createNote(noteRequest: NoteRequest): NetworkResult<NoteResponse>
    suspend fun updateNote(noteId: String, noteRequest: NoteRequest): NetworkResult<NoteResponse>
    suspend fun deleteNote(noteId: String): NetworkResult<NoteResponse>
}