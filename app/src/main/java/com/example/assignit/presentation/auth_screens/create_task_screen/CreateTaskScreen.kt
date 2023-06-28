package com.example.assignit.presentation.auth_screens.create_task_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.common.composables.CustomOutlinedTextFieldLogin
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.model.User

@Composable
fun CreateTaskScreen(
    groupId: String?,
    openAndPopUp: (String, String) -> Unit,
    navigate: (String) -> Unit,
    viewModel: CreateTaskViewModel = hiltViewModel()
) {

    viewModel.setGroupId(groupId)
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    } else {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            CustomOutlinedTextFieldLogin(
                label = "Title",
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                onAction = { focusManager.moveFocus(FocusDirection.Down) },
            )

            CustomOutlinedTextFieldLogin(
                label = "Description",
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                onAction = { focusManager.moveFocus(FocusDirection.Down) },
            )

            UserSelection(
                allUsers = uiState.group?.members ?: emptyList(),
                selectedUsers = uiState.assignees ?: emptyList(),
                onUserSelected = viewModel::onUserSelected,
                onUserDeselected = viewModel::onUserDeselected
            )
        }
    }
}


@Composable
fun UserSelection(
    allUsers: List<User>,
    selectedUsers: List<User>,
    onUserSelected: (User) -> Unit,
    onUserDeselected: (User) -> Unit
) {
    LazyColumn {
        items(allUsers) { user ->
            val checkedState = rememberSaveable { mutableStateOf(selectedUsers.contains(user)) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        if (it) {
                            onUserSelected(user)
                            Log.d("UserSelection", "Checkbox checked for user ${user.username}")
                        } else {
                            onUserDeselected(user)
                            Log.d("UserSelection", "Checkbox unchecked for user ${user.username}")
                        }
                    }
                )
                Text(user.username, Modifier.clickable {
                    checkedState.value = !checkedState.value
                    if (checkedState.value) {
                        onUserSelected(user)
                        Log.d("UserSelection", "Text clicked and user ${user.username} selected")
                    } else {
                        onUserDeselected(user)
                        Log.d("UserSelection", "Text clicked and user ${user.username} deselected")
                    }
                })
            }
        }
    }
}



