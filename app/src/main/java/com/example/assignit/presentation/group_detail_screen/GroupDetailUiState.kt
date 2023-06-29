package com.example.assignit.presentation.group_detail_screen

import com.example.assignit.model.Group
import com.example.assignit.model.User

data class GroupDetailUiState(
    val group: Group? = null,
    val members: List<User> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

