package com.example.assignit.presentation.create_task_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignit.model.Task
import com.example.assignit.model.TaskDescription
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState

    init {
        savedStateHandle.get<String>("groupId")?.let { groupId ->
            _uiState.value = _uiState.value.copy(groupId = groupId)
            loadGroup()
        }
    }

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun addDescriptionField() {
        val updatedDescriptions = _uiState.value.descriptions.toMutableList().apply { add(
            TaskDescription(id = UUID.randomUUID().toString(), description = "", done = false)
        ) }
        _uiState.value = _uiState.value.copy(descriptions = updatedDescriptions)
    }

    fun removeDescriptionField(descriptionId: String) {
        val updatedDescriptions = _uiState.value.descriptions.filterNot { it.id == descriptionId }
        _uiState.value = _uiState.value.copy(descriptions = updatedDescriptions)
    }

    fun onDescriptionChange(descriptionId: String, value: String) {
        val updatedDescriptions = _uiState.value.descriptions.map { if (it.id == descriptionId) it.copy(description = value) else it }
        _uiState.value = _uiState.value.copy(descriptions = updatedDescriptions)
    }

    fun onDescriptionDoneToggle(descriptionId: String) {
        val updatedDescriptions = _uiState.value.descriptions.map { if (it.id == descriptionId) it.copy(done = !it.done) else it }
        _uiState.value = _uiState.value.copy(descriptions = updatedDescriptions)
    }

    fun onDueDateChange(value: Date) {
        _uiState.value = _uiState.value.copy(dueDate = value)
    }

    fun onUserSelected(user: User) {
        val updatedAssignees = (_uiState.value.assignees ?: emptyList()).toMutableList().apply { add(user) }
        _uiState.value = _uiState.value.copy(assignees = updatedAssignees)
    }

    fun onUserDeselected(user: User) {
        val updatedAssignees = (_uiState.value.assignees ?: emptyList()).toMutableList().apply { remove(user) }
        _uiState.value = _uiState.value.copy(assignees = updatedAssignees)
    }

    fun createTask() {
        val task = _uiState.value.groupId?.let {
            Task(
                id = UUID.randomUUID().toString(),
                groupId = it,
                title = _uiState.value.title,
                descriptions = _uiState.value.descriptions,
                assignees = _uiState.value.assignees ?: emptyList(),
                dueDate = _uiState.value.dueDate ?: Date()
            )
        }

        viewModelScope.launch {
            when (val resource = task?.let { taskRepository.createTask(it) }) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(group = resource.data, isLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = resource.message, isLoading = false)
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Something went wrong")
                }
            }
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