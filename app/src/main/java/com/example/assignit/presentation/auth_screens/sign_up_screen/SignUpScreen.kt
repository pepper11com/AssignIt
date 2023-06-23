package com.example.assignit.presentation.auth_screens.sign_up_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.R
import com.example.assignit.common.composables.CustomOutlinedTextField
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.ui.theme.InvalidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val confirmPasswordVisibility = rememberSaveable { mutableStateOf(false) }
    val areAllFieldsValid by viewModel.areAllFieldsValid.collectAsState()



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
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Icon(
                        modifier = Modifier
                            .size(100.dp),
                        painter = painterResource(id = R.drawable.assignit),
                        contentDescription = "App Icon"
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
                .fillMaxSize()
                .padding(it)
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                item {
                    LoadingIndicator()
                }
            } else {
                item {
                    /*
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = uiState.username,
                        onValueChange = viewModel::onUsernameChange,
                        label = { Text("Username") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        trailingIcon = {
                            when (uiState.isUsernameValid) {
                                ValidationState.Valid -> Icon(
                                    Icons.Filled.CheckCircle,
                                    tint = ValidColor,
                                    contentDescription = "Username available"
                                )

                                ValidationState.Invalid -> Icon(
                                    Icons.Filled.Error,
                                    tint = InvalidColor,
                                    contentDescription = "Username taken"
                                )

                                ValidationState.InProgress -> CircularProgressIndicator(
                                    Modifier.size(
                                        20.dp
                                    ), strokeWidth = 1.dp
                                )

                                else -> Unit
                            }
                        },
                        supportingText = {
                            when (uiState.isUsernameValid) {
                                ValidationState.Valid -> Text(
                                    "Username available",
                                    color = ValidColor
                                )

                                ValidationState.Invalid -> Text(
                                    "Username taken",
                                    color = InvalidColor
                                )

                                else -> Text("")
                            }
                        },
                        colors =
                        when (uiState.isUsernameValid) {
                            ValidationState.Valid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = ValidColor,
                                unfocusedBorderColor = ValidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    TextSelectionColors,
                                    TextSelectionColors
                                )
                            )

                            ValidationState.Invalid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = InvalidColor,
                                unfocusedBorderColor = InvalidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    TextSelectionColors,
                                    TextSelectionColors
                                )
                            )

                            else -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    TextSelectionColors,
                                    TextSelectionColors
                                )
                            )
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        trailingIcon = {
                            when (uiState.isEmailValid) {
                                ValidationState.Valid -> Icon(
                                    Icons.Filled.CheckCircle,
                                    tint = ValidColor,
                                    contentDescription = "Email available"
                                )

                                ValidationState.Invalid -> Icon(
                                    Icons.Filled.Error,
                                    tint = InvalidColor,
                                    contentDescription = "Email taken"
                                )

                                ValidationState.InProgress -> CircularProgressIndicator(
                                    Modifier.size(
                                        20.dp
                                    ), strokeWidth = 1.dp
                                )

                                else -> Unit
                            }
                        },
                        supportingText = {
                            when (uiState.isEmailValid) {
                                ValidationState.Valid -> Text(
                                    "Email available",
                                    color = ValidColor
                                )

                                ValidationState.Invalid -> Text(
                                    "Email taken or invalid",
                                    color = InvalidColor
                                )

                                else -> Text("")
                            }
                        },
                        colors =
                        when (uiState.isEmailValid) {
                            ValidationState.Valid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = ValidColor,
                                unfocusedBorderColor = ValidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )

                            ValidationState.Invalid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = InvalidColor,
                                unfocusedBorderColor = InvalidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )

                            else -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )
                        }
                    )


                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        trailingIcon = {
                            Row (
                                modifier = Modifier.padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ){
                                IconButton(
                                    onClick = {
                                        passwordVisibility.value = !passwordVisibility.value
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                                    )
                                }
                                when (uiState.isPasswordValid) {
                                    ValidationState.Valid -> Icon(
                                        Icons.Filled.CheckCircle,
                                        tint = ValidColor,
                                        contentDescription = "Password is valid"
                                    )
                                    ValidationState.Invalid -> Icon(
                                        Icons.Filled.Error,
                                        tint = InvalidColor,
                                        contentDescription = "Password is invalid"
                                    )
                                    else -> Unit
                                }
                            }
                        },
                        supportingText = {
                            when (uiState.isPasswordValid) {
                                ValidationState.Valid -> Text(
                                    "Password is valid",
                                    color = ValidColor
                                )
                                ValidationState.Invalid -> Text(
                                    "Password is invalid",
                                    color = InvalidColor
                                )
                                else -> Text("")
                            }
                        },
                        colors =
                        when (uiState.isPasswordValid) {
                            ValidationState.Valid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = ValidColor,
                                unfocusedBorderColor = ValidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )
                            ValidationState.Invalid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = InvalidColor,
                                unfocusedBorderColor = InvalidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )
                            else -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = uiState.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        label = { Text("Confirm Password") },
                        visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        trailingIcon = {
                            Row (
                                modifier = Modifier.padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ){
                                IconButton(
                                    onClick = {
                                        confirmPasswordVisibility.value =
                                            !confirmPasswordVisibility.value
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (confirmPasswordVisibility.value) "Hide password" else "Show password"
                                    )
                                }
                                when (uiState.isConfirmPasswordValid) {
                                    ValidationState.Valid -> Icon(
                                        Icons.Filled.CheckCircle,
                                        tint = ValidColor,
                                        contentDescription = "Password matches"
                                    )
                                    ValidationState.Invalid -> Icon(
                                        Icons.Filled.Error,
                                        tint = InvalidColor,
                                        contentDescription = "Password doesn't match"
                                    )
                                    else -> Unit
                                }
                            }
                        },
                        supportingText = {
                            when (uiState.isConfirmPasswordValid) {
                                ValidationState.Valid -> Text(
                                    "Password matches",
                                    color = ValidColor
                                )
                                ValidationState.Invalid -> Text(
                                    "Password doesn't match",
                                    color = InvalidColor
                                )
                                else -> Text("")
                            }
                        },
                        colors =
                        when (uiState.isConfirmPasswordValid) {
                            ValidationState.Valid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = ValidColor,
                                unfocusedBorderColor = ValidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )

                            ValidationState.Invalid -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                focusedBorderColor = InvalidColor,
                                unfocusedBorderColor = InvalidColor,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )

                            else -> TextFieldDefaults.outlinedTextFieldColors(
                                focusedLabelColor = Color.White,
                                cursorColor = Color.White,
                                selectionColors = TextSelectionColors(
                                    MediumGrey,
                                    MediumGrey
                                )
                            )
                        }
                    )

                     */

                    CustomOutlinedTextField(
                        label = "Username",
                        value = uiState.username,
                        onValueChange = viewModel::onUsernameChange,
                        validationState = uiState.isUsernameValid,
                        onAction = { focusManager.moveFocus(FocusDirection.Down) },
                        validText = "Username available",
                        invalidText = "Username taken",
                    )

                    CustomOutlinedTextField(
                        label = "Email",
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        validationState = uiState.isEmailValid,
                        onAction = { focusManager.moveFocus(FocusDirection.Down) },
                        validText = "Email available",
                        invalidText = "Email taken or invalid",
                    )

                    CustomOutlinedTextField(
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

                    CustomOutlinedTextField(
                        label = "Confirm Password",
                        value = uiState.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        validationState = uiState.isConfirmPasswordValid,
                        onAction = { focusManager.clearFocus() },
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