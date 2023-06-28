package com.example.assignit.presentation.auth_screens.create_task_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.model.User
import com.example.assignit.repository.GroupRepository
import com.example.assignit.repository.TaskRepository
import com.example.assignit.repository.UserRepository
import com.example.assignit.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val taskRepository: TaskRepository,

) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState


    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value)
    }

    fun onAssigneesChange(value: List<User>) {
        _uiState.value = _uiState.value.copy(assignees = value)
    }

    fun onDueDateChange(value: Date) {
        _uiState.value = _uiState.value.copy(dueDate = value)
    }

    fun onUserSelected(user: User) {
        _uiState.value = _uiState.value.copy(assignees = (_uiState.value.assignees?.toMutableList()?.apply { add(user) })?.toList())
    }

    fun onUserDeselected(user: User) {
        _uiState.value = _uiState.value.copy(assignees = (_uiState.value.assignees?.toMutableList()?.apply { remove(user) })?.toList())
    }



    fun setGroupId(groupId: String?) {
        if (groupId != null && groupId != _uiState.value.groupId) {
            _uiState.value = _uiState.value.copy(groupId = groupId)
            loadGroup()
        }
    }


    private fun loadGroup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val groupResource = _uiState.value.groupId?.let { groupRepository.getGroup(it) }) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(group = groupResource.data, isLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = groupResource.message, isLoading = false)
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Something went wrong")
                }
            }
        }
    }
}