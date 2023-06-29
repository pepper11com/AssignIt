package com.example.assignit.model

import java.util.Date

data class Task(
    val id: String,
    val groupId: String,
    val title: String,
    val descriptions: List<TaskDescription>,
    val assigneeIds: List<String>,
    val dueDate: Date,
)