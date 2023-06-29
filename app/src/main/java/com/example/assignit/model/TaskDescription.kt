package com.example.assignit.model

data class TaskDescription(
    val id: String,
    val description: String,
    var done: Boolean = false,
)

