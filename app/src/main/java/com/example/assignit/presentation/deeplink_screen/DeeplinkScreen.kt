package com.example.assignit.presentation.deeplink_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.presentation.DEEPLINK_SCREEN
import com.example.assignit.presentation.HOME_SCREEN

@Composable
fun DeeplinkScreen(
    groupId: String,
    openAndPopUp: (String, String) -> Unit,
    viewModel: DeeplinkViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) {
            LoadingIndicator()
        }

        uiState.error?.let { error ->
            Text(text = error) // Displaying error
        }

        Text(text = "Deeplink Screen $groupId")

        Row {

            Button(
                onClick = { viewModel.joinGroup(groupId) },
                enabled = !uiState.isLoading // Disabling button during loading
            ) {
                Text("Join Group")
            }

            Button(
                onClick = {
                    openAndPopUp(HOME_SCREEN, DEEPLINK_SCREEN)
                }
            ) {
                Text("Cancel")
            }
        }
    }

}
