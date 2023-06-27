package com.example.assignit.model

data class User(
    val id: String? = null,
    val username: String = "",
    val email: String? = null,
    val groups: List<String> = listOf(),
)

data class LoginUiState(
    val email: String = "",
    val password: String = "",
)

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val email: String?,
    val username: String?,
    val profilePictureUrl: String?,
)
