package com.example.assignit.presentation.deeplink_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.repository.GroupRepository
import com.example.assignit.repository.UserRepository
import com.example.assignit.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeeplinkViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DeeplinkUiState())
    val uiState: StateFlow<DeeplinkUiState> = _uiState

    fun joinGroup(groupId: String) {
        _uiState.value = DeeplinkUiState(isLoading = true)

        viewModelScope.launch {
            val firebaseUser = userRepository.currentUser.value ?: return@launch
            Log.d("DeeplinkViewModel", "joinGroup: ${firebaseUser.email}")

            when (val userResource = userRepository.getUserDataById(firebaseUser.uid)) {
                is Resource.Success -> {

                    Log.d("DeeplinkViewModel", "joinGroup: ${userResource.data}")

                    val user = userResource.data
                    if (user != null) {
                        Log.d("DeeplinkViewModel", "groupId: ${groupId}")
                        groupRepository.addUserToGroup(groupId, user)
                        _uiState.value = DeeplinkUiState() // reset state after successful operation
                    } else {
                        _uiState.value = DeeplinkUiState(error = "Failed to get user data.")
                    }
                }
                is Resource.Error -> {
                    Log.d("DeeplinkViewModel", "joinGroupERROR: ${userResource.message}")
                    _uiState.value = DeeplinkUiState(error = userResource.message)
                }
                else -> {
                    // Handle loading
                }
            }
        }
    }

}
