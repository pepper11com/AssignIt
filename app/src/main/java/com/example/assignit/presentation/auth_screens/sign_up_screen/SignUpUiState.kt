package com.example.assignit.presentation.auth_screens.sign_up_screen

data class SignUpUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,

    val isUsernameValid: ValidationState = ValidationState.NotChecked,
    val isEmailValid: ValidationState = ValidationState.NotChecked,
    val isPasswordValid: ValidationState = ValidationState.NotChecked,
    val isConfirmPasswordValid: ValidationState = ValidationState.NotChecked,
)

enum class ValidationState {
    NotChecked, Valid, Invalid, InProgress
}