package com.example.assignit.presentation.auth_screens.sign_up_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.common.ext.isValidEmail
import com.example.assignit.common.ext.isValidPassword
import com.example.assignit.common.ext.passwordMatches
import com.example.assignit.model.SignInResult
import com.example.assignit.model.User
import com.example.assignit.repository.UserRepository
import com.example.assignit.util.resource.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    private val _googleAuthenticationState = MutableStateFlow<Resource<Unit>>(Resource.Empty())
    val googleAuthenticationState: StateFlow<Resource<Unit>> get() = _googleAuthenticationState

    private val _googleUsernameAuthenticationState = MutableStateFlow<Resource<Unit>>(Resource.Empty())
    val googleUsernameAuthenticationState: StateFlow<Resource<Unit>> get() = _googleUsernameAuthenticationState



    private var validationJob: Job? = null

    val areAllFieldsValid: StateFlow<Boolean> = _uiState.map { uiState ->
        uiState.isEmailValid == ValidationState.Valid &&
                uiState.isUsernameValid == ValidationState.Valid &&
                uiState.isPasswordValid == ValidationState.Valid &&
                uiState.isConfirmPasswordValid == ValidationState.Valid
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    private val email
        get() = uiState.value.email
    private val username
        get() = uiState.value.username
    private val password
        get() = uiState.value.password
    private val confirmPassword
        get() = uiState.value.confirmPassword

    fun onEmailChange(newValue: String) {
        validationJob?.cancel()
        _uiState.value = _uiState.value.copy(email = newValue, isEmailValid = ValidationState.InProgress)
        validationJob = viewModelScope.launch {
            delay(750)
            _uiState.value = _uiState.value.copy(
                isEmailValid = if (repository.isEmailAvailable(newValue) && newValue.isValidEmail()) {
                    ValidationState.Valid
                } else {
                    ValidationState.Invalid
                }
            )
        }
    }

    fun onUsernameChange(newValue: String) {
        validationJob?.cancel()
        _uiState.value = _uiState.value.copy(username = newValue, isUsernameValid = ValidationState.InProgress)
        validationJob = viewModelScope.launch {
            delay(750)
            _uiState.value = _uiState.value.copy(
                isUsernameValid = if (repository.isUsernameAvailable(newValue) && newValue.isNotEmpty() && !newValue.isValidEmail()) {
                    ValidationState.Valid
                } else {
                    ValidationState.Invalid
                }
            )
        }
    }

    fun onGoogleUsernameChange(newValue: String) {
        Log.d("SignUpViewModel", "onGoogleUsernameChange: $newValue")
        validationJob?.cancel()
        _uiState.value = _uiState.value.copy(username = newValue, isUsernameValid = ValidationState.InProgress)
        validationJob = viewModelScope.launch {
            delay(750)
            _uiState.value = _uiState.value.copy(
                isUsernameValid = if (repository.isUsernameAvailable(newValue) && newValue.isNotEmpty() && !newValue.isValidEmail()) {
                    ValidationState.Valid
                } else {
                    ValidationState.Invalid
                }
            )
        }
    }
//    fun onGoogleUsernameChange(newValue: String) {
//        validationJob?.cancel()
//        _googleUsername.value = newValue
//        validationJob = viewModelScope.launch {
//            delay(750)
//            _uiState.value = _uiState.value.copy(
//                isUsernameValid = if (repository.isUsernameAvailable(newValue) && newValue.isNotEmpty() && !newValue.isValidEmail()) {
//                    ValidationState.Valid
//                } else {
//                    ValidationState.Invalid
//                }
//            )
//        }
//    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(
            password = newValue,
            isPasswordValid = if (newValue.isValidPassword()) {
                ValidationState.Valid
            } else {
                ValidationState.Invalid
            }
        )
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = newValue,
            isConfirmPasswordValid = if (newValue.passwordMatches(_uiState.value.password)) {
                ValidationState.Valid
            } else {
                ValidationState.Invalid
            }
        )
    }

    fun onGoogleSignInClick(result: SignInResult) {
        _googleAuthenticationState.value = Resource.Loading()
        Log.d("SignUpViewModel", "onGoogleSignInClick: $result")
        if (result.data != null) {
            _googleAuthenticationState.value = Resource.Success(Unit)
        } else {
            _googleAuthenticationState.value = Resource.Error(result.errorMessage ?: "Unknown error")
        }
    }

    fun createGoogleUser() {
        _googleUsernameAuthenticationState.value = Resource.Loading()

        val currentUsername = username
        Log.d("SignUpViewModel", "createGoogleUser: $currentUsername")

        viewModelScope.launch {

            val currentUser = repository.getGoogleUser()
            Log.d("SignUpViewModel", "createGoogleUser: $currentUser")

            val newUser = User(
                id = currentUser.data?.userId ?: "",
                email = currentUser.data?.email ?: "",
                username = currentUsername
            )

            Log.d("SignUpViewModel", "createGoogleUser: $newUser")

            when (val result = repository.createGoogleUser(newUser)) {
                is Resource.Success -> {
                    // handle user creation result
                    _googleUsernameAuthenticationState.value = Resource.Success(Unit)
                    Log.d("SignUpViewModel", "createGoogleUser: Success")
                }
                is Resource.Error -> {
                    // handle user creation result
                    _googleUsernameAuthenticationState.value = Resource.Error(result.message ?: "Unknown error")
                    Log.d("SignUpViewModel", "createGoogleUser: Error")
                }

                else -> {}
            }
        }
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