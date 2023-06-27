package com.example.assignit.presentation.group_screen

import com.example.assignit.model.Group

data class GroupUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val groups: List<Group> = listOf()
)
