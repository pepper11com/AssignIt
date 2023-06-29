package com.example.assignit.presentation.group_screen

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
class GroupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState

     fun fetchGroupsForUser() {
        viewModelScope.launch {
            _uiState.value = GroupUiState(isLoading = true)
            try {
                // fetch the current user
                val user = userRepository.getCurrentUser()
                Log.d("GroupViewModel", "user: $user")
                val groupResources = user.groups.mapNotNull { groupId ->
                    // fetch the group for each groupId and return it
                    Log.d("GroupViewModel", "groupId: $groupId")
                    when (val groupResource = groupRepository.getGroup(groupId)) {
                        is Resource.Success -> groupResource.data
                        is Resource.Error -> {
                            Log.e("GroupViewModel", "Error fetching group", Exception(groupResource.message))
                            null
                        }
                        else -> {
                            Log.e("GroupViewModel", "Error fetching group", Exception("Unknown error"))
                            null
                        }
                    }
                }
                _uiState.value = GroupUiState(isLoading = false, groups = groupResources)
                Log.d("GroupViewModel", "_uiState.value: $_uiState.value")
            } catch (e: Exception) {
                _uiState.value = GroupUiState(isLoading = false, error = e.message)
            }
        }
    }


}
