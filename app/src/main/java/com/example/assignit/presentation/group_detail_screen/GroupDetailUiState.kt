package com.example.assignit.presentation.group_detail_screen

import com.example.assignit.model.Group

data class GroupDetailUiState(
    val group: Group? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
