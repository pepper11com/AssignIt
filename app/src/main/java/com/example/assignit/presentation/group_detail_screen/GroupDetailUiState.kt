package com.example.assignit.presentation.group_detail_screen

import com.example.assignit.model.Group
import com.example.assignit.model.Task
import com.example.assignit.model.User

data class GroupDetailUiState(
    val group: Group? = null,
    val members: List<User> = listOf(),
    val groupTasks: List<TaskWithAssigneeNames> = listOf(),
    val memberTasks: List<Task> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class TaskWithAssigneeNames(
    val task: Task,
    val assigneeNames: List<String>
)