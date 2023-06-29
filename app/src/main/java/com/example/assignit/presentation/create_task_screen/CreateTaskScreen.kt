package com.example.assignit.presentation.create_task_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.common.composables.CustomOutlinedTextFieldLogin
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.model.User
import com.example.assignit.presentation.CREATE_TASK_SCREEN
import com.example.assignit.ui.theme.DarkGrey
import com.example.assignit.ui.theme.InvalidColor
import com.example.assignit.ui.theme.MediumGrey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    groupId: String?,
    openAndPopUp: (String, String) -> Unit,
    navigate: (String) -> Unit,
    viewModel: CreateTaskViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()

    val openDateDialog = remember { mutableStateOf(false) }
    val openAssignDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.group?.name ?: "Task")
                },
                navigationIcon = {
                    //TODO: Add the back button here
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.createTask()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                LoadingIndicator()
            }

            uiState.error?.let {
                Text(text = it, color = InvalidColor)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                item {
                    CustomOutlinedTextFieldLogin(
                        label = "Title",
                        value = uiState.title,
                        onValueChange = viewModel::onTitleChange,
                        onAction = { focusManager.moveFocus(FocusDirection.Down) },
                    )
                }

                items(uiState.descriptions) { description ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 5.dp),
                    ) {
                        OutlinedTextField(
                            value = description.description,
                            onValueChange = { viewModel.onDescriptionChange(description.id, it) },
                            label = { Text("Todo") },
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey, MediumGrey
                                ),
                                focusedLabelColor = Color.White,
                            ),
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(
                            onClick = { viewModel.removeDescriptionField(description.id) },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Remove, contentDescription = null)
                        }
                    }
                }

                item {
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ){
                        IconButton(
                            onClick = { viewModel.addDescriptionField() },

                            ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        }
                    }
                }

                item {
                    CustomAssigneeCard(
                        onClick = {
                            openAssignDialog.value = true
                        },
                        text = "Assign to",
                        assignees = uiState.assignees ?: emptyList()
                    )
                }

                item {
                    CustomDateCard(
                        onClick = {
                            openDateDialog.value = true
                        },
                        text = "Due Date",
                        value = uiState.dueDate?.let {
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                        } ?: ""
                    )
                }

                item {
                    UserSelection(
                        allUsers = uiState.group?.members ?: emptyList(),
                        selectedUsers = uiState.assignees ?: emptyList(),
                        onUserSelected = viewModel::onUserSelected,
                        onUserDeselected = viewModel::onUserDeselected,
                        openDialog = openAssignDialog,
                        lazyListState = lazyListState
                    )
                }

                item {
                    CustomDatePicker(
                        openDialog = openDateDialog,
                        onDateSelected = viewModel::onDueDateChange
                    )
                }



            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    openDialog: MutableState<Boolean>,
    onDateSelected: (Date) -> Unit
) {
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(Date(millis))
                        }
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = DarkGrey,
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = DarkGrey,
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSelection(
    allUsers: List<User>,
    selectedUsers: List<User>,
    onUserSelected: (User) -> Unit,
    onUserDeselected: (User) -> Unit,
    openDialog: MutableState<Boolean>,
    lazyListState: LazyListState
) {
    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.background(color = DarkGrey, shape = RoundedCornerShape(8.dp)),
            onDismissRequest = {
                openDialog.value = false
            },
        ) {
            LazyColumn (
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item{
                    Text(
                        text = "Assign to",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                }
                items(allUsers) { user ->
                    val checkedState = rememberSaveable { mutableStateOf(selectedUsers.contains(user)) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                checkedState.value = !checkedState.value
                                if (checkedState.value) {
                                    onUserSelected(user)
                                } else {
                                    onUserDeselected(user)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it
                                if (it) {
                                    onUserSelected(user)
                                } else {
                                    onUserDeselected(user)
                                }
                            }
                        )
                        Text(
                            user.username,
                        )
                    }
                }
            }
        }
    }
}
