package com.vm.vishunotesapp.models

data class NoteResponse(
    val __v: Int,
    val createdAt: String,
    val description: String,
    val id: String,
    val title: String,
    val updatedAt: String,
    val userId: String
)