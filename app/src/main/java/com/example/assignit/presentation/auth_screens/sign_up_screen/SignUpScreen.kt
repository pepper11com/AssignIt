package com.example.assignit.presentation.auth_screens.sign_up_screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.R
import com.example.assignit.common.composables.CustomOutlinedTextFieldSignUp
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.presentation.SIGN_UP_SCREEN
import com.example.assignit.presentation.SIGN_UP_USERNAME_SCREEN
import com.example.assignit.services.GoogleAuth
import com.example.assignit.ui.theme.DarkOrange
import com.example.assignit.ui.theme.InvalidColor
import com.example.assignit.util.resource.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    googleAuthUiClient: GoogleAuth,
    openAndPopUp: (String, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val confirmPasswordVisibility = rememberSaveable { mutableStateOf(false) }
    val areAllFieldsValid by viewModel.areAllFieldsValid.collectAsState()
    val googleAuthenticationState by viewModel.googleAuthenticationState.collectAsState()

    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onGoogleSignInClick(signInResult)
                }
            }
        }
    )

    when(googleAuthenticationState){
        is Resource.Success -> {
            openAndPopUp(SIGN_UP_USERNAME_SCREEN, SIGN_UP_SCREEN)
        }

        else -> Unit
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                title = {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        painter = painterResource(id = R.drawable.ph_note_pencil),
                        contentDescription = "App Icon",
                        tint = DarkOrange
                    )
                },
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(68.dp),
                containerColor = Color.Black,
                contentColor = Color.White,
            ) {
                Column {
                    Divider(
                        thickness = Dp.Hairline,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 11.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                /*TODO*/
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Back")
                        }
                        OutlinedButton(
                            onClick = {
                                if (areAllFieldsValid) {
                                    viewModel.signUp()
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (areAllFieldsValid) Color.White else InvalidColor
                            ),
                            enabled = areAllFieldsValid
                        ) {
                            Text(text = "Sign Up")
                        }

                    }
                }
            }
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                item {
                    LoadingIndicator()
                }
            } else {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Start,
                            text = "Create an account",
                            color = Color.White,
                        )
                    }

                    TextButton(
                        onClick = {
                            scope.launch {
                                val signInIntentSender = googleAuthUiClient.signInWithGoogle()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    ) {
                        Text(
                            "Create account with Google",
                            color = Color.White
                        )
                    }


                    CustomOutlinedTextFieldSignUp(
                        label = "Username",
                        value = uiState.username,
                        onValueChange = viewModel::onUsernameChange,
                        validationState = uiState.isUsernameValid,
                        onAction = { focusManager.moveFocus(FocusDirection.Down) },
                        validText = "Username available",
                        invalidText = "Username taken",
                    )

                    CustomOutlinedTextFieldSignUp(
                        label = "Email",
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        validationState = uiState.isEmailValid,
                        onAction = { focusManager.moveFocus(FocusDirection.Down) },
                        validText = "Email available",
                        invalidText = "Email taken or invalid",
                    )

                    CustomOutlinedTextFieldSignUp(
                        label = "Password",
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        validationState = uiState.isPasswordValid,
                        onAction = { focusManager.moveFocus(FocusDirection.Down) },
                        keyboardType = KeyboardType.Password,
                        passwordVisibility = passwordVisibility,
                        showPasswordToggle = true,
                        validText = "Password is valid",
                        invalidText = "Password is invalid",
                    )

                    //todo test this onAction = { focusManager.moveFocus(FocusDirection.Exit) },
                    CustomOutlinedTextFieldSignUp(
                        label = "Confirm Password",
                        value = uiState.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        validationState = uiState.isConfirmPasswordValid,
                        onAction = { focusManager.moveFocus(FocusDirection.Exit) },
                        keyboardType = KeyboardType.Password,
                        passwordVisibility = confirmPasswordVisibility,
                        showPasswordToggle = true,
                        action = ImeAction.Done,
                        validText = "Password matches",
                        invalidText = "Password doesn't match",
                    )

                }

                item {
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "An unexpected error occurred",
                            color = InvalidColor
                        )
                    }
                }

            }
        }
    }
}