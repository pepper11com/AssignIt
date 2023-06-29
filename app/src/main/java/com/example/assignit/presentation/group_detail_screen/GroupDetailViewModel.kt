package com.example.assignit.presentation.group_detail_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val taskRepository: TaskRepository,
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
                        val members = groupResource.data?.let { fetchMembers(it.memberIds) } ?: emptyList()

                        // todo  groupResource contains group data inside is the taskIds, for each taskIds, fetch task and assignee names

                        val groupTasks = groupResource.data?.let {
                            taskRepository.getTasksByIds(it.taskIds)
                        } ?: emptyList()

                        Log.d("GroupDetailViewModel", "groupTasks: $groupTasks")

                        val groupTasksWithAssigneeNames = groupTasks.map { task ->
                            val assigneeNames = task.assigneeIds.mapNotNull { id ->
                                when (val userResource = userRepository.getUserDataById(id)) {
                                    is Resource.Success -> userResource.data?.username
                                    else -> null
                                }
                            }
                            Log.d("GroupDetailViewModel", "assigneeNames: $assigneeNames")

                            TaskWithAssigneeNames(task, assigneeNames)
                        }
                        _uiState.value = GroupDetailUiState(group = groupResource.data, members = members, groupTasks = groupTasksWithAssigneeNames)
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


    fun fetchTasksForUser(user: User) {
        viewModelScope.launch {
            val userTasks = taskRepository.getTasksByUser(user.id!!)
            _uiState.value = _uiState.value.copy(memberTasks = userTasks)
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

