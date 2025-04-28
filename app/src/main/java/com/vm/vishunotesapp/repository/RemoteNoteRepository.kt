package com.vm.vishunotesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vm.vishunotesapp.models.NoteRequest
import com.vm.vishunotesapp.models.NoteResponse
import com.vm.vishunotesapp.networking.NotesApi
import com.vm.vishunotesapp.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class RemoteNoteRepository @Inject constructor(private val notesApi: NotesApi) : NoteRepository {
    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    override val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>>
        get() = _notesLiveData

    override suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())
        val notesResponse = notesApi.getNotes()

        with (notesResponse) {
            if (this.isSuccessful && body() != null) {
                _notesLiveData.postValue(NetworkResult.Success(body()!!))
            }
            else if (errorBody() != null) {
                _notesLiveData.postValue(NetworkResult.Error(errorBody()!!.string()))
            } else {
                _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        }
    }

    override suspend fun createNote(noteRequest: NoteRequest): NetworkResult<NoteResponse> {
        val noteResponse = notesApi.createNote(noteRequest)
        return handleNoteResponse(noteResponse)
    }

    override suspend fun updateNote(noteId: String, noteRequest: NoteRequest): NetworkResult<NoteResponse> {
        val noteResponse = notesApi.updateNote(noteId, noteRequest)
        return handleNoteResponse(noteResponse)
    }

    override suspend fun deleteNote(noteId: String): NetworkResult<NoteResponse> {
        val noteResponse = notesApi.deleteNote(noteId)
        return handleNoteResponse(noteResponse)
    }

    private fun handleNoteResponse(response: Response<NoteResponse>): NetworkResult<NoteResponse> {
        if (response.isSuccessful && response.body() != null) {
            return NetworkResult.Success(response.body()!!)
        } else {
            return NetworkResult.Error("Something went wrong")
        }
    }
}