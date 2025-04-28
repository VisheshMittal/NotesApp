package com.vm.vishunotesapp.networking

import com.vm.vishunotesapp.models.NoteRequest
import com.vm.vishunotesapp.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesApi {

    @GET("/notes")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("/notes/{id}")
    suspend fun updateNote(@Path("id") noteId: String, @Body noteRequest: NoteRequest): Response<NoteResponse>

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") noteId: String): Response<NoteResponse>
}