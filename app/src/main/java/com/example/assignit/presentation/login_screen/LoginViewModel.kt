package com.example.assignit.presentation.login_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.common.ext.isValidEmail
import com.example.assignit.model.SignInResult
import com.example.assignit.presentation.HOME_SCREEN
import com.example.assignit.presentation.LOGIN_SCREEN
import com.example.assignit.presentation.SIGN_UP_USERNAME_SCREEN
import com.example.assignit.repository.UserRepository
import com.example.assignit.services.GoogleAuth
import com.example.assignit.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val googleLogin: GoogleAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    val areAllFieldsValid: StateFlow<Boolean> = _uiState.map { uiState ->
        uiState.userInput.isValidEmail() || uiState.userInput.isNotEmpty() &&
                uiState.password.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // Handle the change of email or username
    fun onEmailOrUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            userInput = value.trim(),
        )
    }

    // Handle the change of password
    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onGoogleSignInClick(result: SignInResult, openAndPopUp: (String, String) -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        Log.d("SignUpViewModel", "onGoogleSignInClick: $result")
        if (result.data != null) {
            viewModelScope.launch {
                val userId = result.data.userId
                Log.d("SignUpViewModel", "User ID: $userId")

                // Getting the user object from the database with the user ID
                when(val userDataResult = repository.getUserDataById(userId)) {
                    is Resource.Success -> {
                        val userData = userDataResult.data
                        if(userData?.username == "" || userData?.username == null) {
                            // Go to pick a username screen
                            Log.d("SignUpViewModel", "userData?.username == null")
                            openAndPopUp(SIGN_UP_USERNAME_SCREEN, LOGIN_SCREEN)
                            _uiState.value = _uiState.value.copy(isLoading = false, error = null)
                        } else {
                            // Go to home screen
                            Log.d("SignUpViewModel", "userData?.username == null ELSE ELSE ELSE")
                            openAndPopUp(HOME_SCREEN, LOGIN_SCREEN)
                            _uiState.value = _uiState.value.copy(isLoading = false, error = null)
                        }
                    }
                    is Resource.Error -> {
                        Log.d("SignUpViewModel", "Resource.Error")
                        _uiState.value = _uiState.value.copy(isLoading = false, error = userDataResult.message ?: "Error fetching user data")
                    }

                    else -> {
                        Log.d("SignUpViewModel", "ELSE ELSE ELSE")
                        _uiState.value = _uiState.value.copy(isLoading = false, error = "Unknown error")
                    }
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, error = result.errorMessage ?: "Unknown error")
        }
    }


    // Handle the login process
    fun onLoginClick() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)

        Log.d("LoginViewModel", "userInput: ${_uiState.value.userInput} PASSWORD: ${_uiState.value.password}")

        if (_uiState.value.userInput.isValidEmail()) {
            Log.d("LoginViewModel", "userInput: ${_uiState.value.userInput}")
            val result = repository.signInWithEmailAndPassword(_uiState.value.userInput, _uiState.value.password)
            handleResult(result)
        } else {
            Log.d("LoginViewModel", "userInput2: ${_uiState.value.userInput} PASSWORD2: ${_uiState.value.password}")
            val result = repository.signInWithUsernameAndPassword(_uiState.value.userInput, _uiState.value.password)
            handleResult(result)
        }
    }

    private fun handleResult(result: Resource<Unit>) {
        _uiState.value = when(result) {
            is Resource.Success ->{
                Log.d("LoginViewModel", "SUCCESS: ${result.data}")
                _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                Log.d("LoginViewModel", "ERROR: ${result.message}")
                _uiState.value.copy(
                    isLoading = false,
                    error = result.message,
                    password = "",
                    userInput = ""
                )
            }
            else -> {
                Log.d("LoginViewModel", "ELSE: ${result.message}")
                _uiState.value.copy(
                    isLoading = false,
                    password = "",
                    userInput = ""
                )
            }
        }
    }

}
