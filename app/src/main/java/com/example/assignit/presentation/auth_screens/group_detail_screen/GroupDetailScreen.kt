package com.example.assignit.presentation.auth_screens.group_detail_screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.model.Group

@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            // Show a loading spinner
            CircularProgressIndicator()
        }
        uiState.group != null -> {
            // Display group's details
            GroupDetailContent(group = uiState.group!!)
        }
        uiState.error.isNotEmpty() -> {
            // Display error message
            Text(text = uiState.error, color = Color.Red)
        }
    }
}

@Composable
fun GroupDetailContent(group: Group) {
    // Fill in with your actual UI code

    Text(text = group.name)
}
