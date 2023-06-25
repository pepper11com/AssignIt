package com.example.assignit.presentation.auth_screens.sign_up_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.assignit.common.composables.CustomOutlinedTextFieldSignUp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginPickUsernameScreen(
    viewModel: SignUpViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomOutlinedTextFieldSignUp(
            label = "Username",
            value = uiState.username,
            onValueChange = viewModel::onGoogleUsernameChange,
            validationState = uiState.isUsernameValid,
            onAction = { focusManager.moveFocus(FocusDirection.Exit) },
            validText = "Username available",
            invalidText = "Username taken",
            action = ImeAction.Done
        )


        Button(
            onClick = {
                viewModel.createGoogleUser()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Confirm")
        }
    }
}
