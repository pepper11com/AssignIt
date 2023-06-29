package com.example.assignit.presentation.group_detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.model.User
import com.example.assignit.repository.GroupRepository
import com.example.assignit.repository.UserRepository
import com.example.assignit.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState: StateFlow<GroupDetailUiState> = _uiState

    init {
        savedStateHandle.get<String>("groupId")?.let { groupId ->
            viewModelScope.launch {
                _uiState.value = GroupDetailUiState(isLoading = true)
                when (val groupResource = groupRepository.getGroup(groupId)) {
                    is Resource.Success -> {
                        val members = groupResource.data?.let { fetchMembers(it.memberIds) }
                        _uiState.value =
                            members?.let { GroupDetailUiState(group = groupResource.data, members = it) }!!
                    }
                    is Resource.Error -> {
                        _uiState.value = GroupDetailUiState(error = "Error loading group details")
                    }
                    else -> {
                        _uiState.value = GroupDetailUiState(error = "Unknown error occurred")
                    }
                }
            }
        }
    }

    private suspend fun fetchMembers(memberIds: List<String>): List<User> {
        return memberIds.mapNotNull { memberId ->
            when (val userResource = userRepository.getUserDataById(memberId)) {
                is Resource.Success -> userResource.data
                else -> null
            }
        }
    }
}

