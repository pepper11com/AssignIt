package com.example.assignit.presentation.sign_up_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.common.ext.isValidEmail
import com.example.assignit.common.ext.isValidPassword
import com.example.assignit.common.ext.passwordMatches
import com.example.assignit.repository.UserRepository
import com.example.assignit.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    private val email
        get() = uiState.value.email
    private val username
        get() = uiState.value.username
    private val password
        get() = uiState.value.password
    private val confirmPassword
        get() = uiState.value.confirmPassword

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onUsernameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(username = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newValue)
    }

    fun signUp() {
        val currentUsername = username
        val currentEmail = email
        val currentPassword = password
        val currentConfirmPassword = confirmPassword

        viewModelScope.launch {
            // Log the username, email, password
            Log.d("SignUpViewModel", "Username: $currentUsername Email: $currentEmail Password: $currentPassword")

            // Check if passwords match, if email is valid, if username is not empty
            if (!currentPassword.passwordMatches(currentConfirmPassword)) {
                _uiState.value = SignUpUiState(error = "Passwords do not match")
                return@launch
            } else if (!currentEmail.isValidEmail()) {
                _uiState.value = SignUpUiState(error = "Please enter a valid email address")
                return@launch
            } else if (!currentPassword.isValidPassword()) {
                _uiState.value = SignUpUiState(error = "Password does not meet the criteria")
                return@launch
            } else if (currentUsername.isEmpty()) {
                _uiState.value = SignUpUiState(error = "Please enter a username")
                return@launch
            }

            _uiState.value = SignUpUiState(isLoading = true)

            // Log the username, email, password again to verify they haven't changed
            Log.d("SignUpViewModel", "Username: $currentUsername Email: $currentEmail Password: $currentPassword")

            when (val result = repository.createUser(currentEmail, currentUsername, currentPassword)) {
                is Resource.Success -> {
                    Log.d("SignUpViewModel", "Successfully created user")
                    _uiState.value = SignUpUiState()
                }
                is Resource.Error -> {
                    Log.d("SignUpViewModel", "Failed to create user: ${result.message}")
                    _uiState.value = SignUpUiState(error = result.message)
                }
                else -> {
                    Log.d("SignUpViewModel", "Failed to create user: Unknown error")
                    _uiState.value = SignUpUiState(error = "Unknown error")
                }
            }

            _uiState.value = SignUpUiState(isLoading = false)
        }
    }




}