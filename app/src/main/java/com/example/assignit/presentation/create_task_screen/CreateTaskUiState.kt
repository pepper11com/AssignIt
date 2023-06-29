package com.example.assignit.presentation.create_task_screen

import com.example.assignit.model.Group
import com.example.assignit.model.TaskDescription
import com.example.assignit.model.User
import java.util.Date
import java.util.UUID

data class CreateTaskUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val id: String = "",
    val title: String = "",
    val descriptions: List<TaskDescription> = listOf(
        TaskDescription(
            id = UUID.randomUUID().toString(), description = "", done = false
        )
    ),
    val assignees: List<User> = emptyList(),
    val memberUsers: List<User> = emptyList(),
    val dueDate: Date? = null,

    val groupId: String? = null,
    val group: Group? = null,
)

