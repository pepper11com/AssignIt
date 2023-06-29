package com.example.assignit.presentation.login_screen

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val userInput: String = "",
    val password: String = "",
)