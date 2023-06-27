package com.example.assignit.presentation.auth_screens.group_detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.repository.GroupRepository
import com.example.assignit.repository.UserRepository
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
                try {
                    val group = groupRepository.getGroup(groupId)
                    _uiState.value = GroupDetailUiState(group = group)
                } catch (e: Exception) {
                    _uiState.value = GroupDetailUiState(error = "Error loading group details")
                }
            }
        }
    }
}
