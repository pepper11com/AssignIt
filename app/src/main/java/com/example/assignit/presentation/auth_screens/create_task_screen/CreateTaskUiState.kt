package com.example.assignit.presentation.auth_screens.create_task_screen

import com.example.assignit.model.Group
import com.example.assignit.model.User
import java.util.Date

data class CreateTaskUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignees: List<User>? = null,
    val dueDate: Date? = null,

    val groupId: String? = null,
    val group: Group? = null,
)
