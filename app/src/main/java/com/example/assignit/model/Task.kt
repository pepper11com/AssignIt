package com.example.assignit.model

import java.util.Date

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val assignees: List<User>,
    val dueDate: Date,
)

