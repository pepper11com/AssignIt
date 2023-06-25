package com.example.assignit.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.assignit.presentation.auth_screens.sign_up_screen.ValidationState
import com.example.assignit.ui.theme.InvalidColor
import com.example.assignit.ui.theme.MediumGrey
import com.example.assignit.ui.theme.ValidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFieldSignUp(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    validationState: ValidationState,
    onAction: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    passwordVisibility: MutableState<Boolean> = remember { mutableStateOf(false) },
    showPasswordToggle: Boolean = false,
    confirmationHint: String = "",
    action: ImeAction = ImeAction.Next,
    tint: Color = if (validationState == ValidationState.Valid) ValidColor else InvalidColor,
    validText: String,
    invalidText: String,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (passwordVisibility.value && showPasswordToggle) VisualTransformation.None else if (showPasswordToggle) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = action, keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(onNext = {
            onAction()
        }),
        trailingIcon = {
            Row(
                modifier = Modifier.padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (showPasswordToggle) {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                        )
                    }
                }
                when (validationState) {
                    ValidationState.Valid -> Icon(
                        Icons.Filled.CheckCircle, tint = tint, contentDescription = confirmationHint
                    )

                    ValidationState.Invalid -> Icon(
                        Icons.Filled.Error, tint = tint, contentDescription = "Invalid"
                    )

                    ValidationState.InProgress -> CircularProgressIndicator(
                        Modifier.size(
                            20.dp
                        ), strokeWidth = 1.dp
                    )

                    else -> Unit
                }
            }
        },
        supportingText = {
            when (validationState) {
                ValidationState.Valid -> Text(
                    validText, color = ValidColor
                )

                ValidationState.Invalid -> Text(
                    invalidText, color = InvalidColor
                )

                else -> Text("")
            }
        },
        colors = when (validationState) {
            ValidationState.Valid -> TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color.White,
                focusedBorderColor = ValidColor,
                unfocusedBorderColor = ValidColor,
                cursorColor = Color.White,
                selectionColors = TextSelectionColors(
                    MediumGrey, MediumGrey
                )
            )

            ValidationState.Invalid -> TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color.White,
                focusedBorderColor = InvalidColor,
                unfocusedBorderColor = InvalidColor,
                cursorColor = Color.White,
                selectionColors = TextSelectionColors(
                    MediumGrey, MediumGrey
                )
            )

            else -> TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color.White,
                cursorColor = Color.White,
                selectionColors = TextSelectionColors(
                    MediumGrey, MediumGrey
                )
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFieldLogin(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onAction: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    passwordVisibility: MutableState<Boolean> = remember { mutableStateOf(false) },
    showPasswordToggle: Boolean = false,
    action: ImeAction = ImeAction.Next,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (passwordVisibility.value && showPasswordToggle) VisualTransformation.None else if (showPasswordToggle) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = action, keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(onNext = {
            onAction()
        }),
        trailingIcon = {
            Row(
                modifier = Modifier.padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (showPasswordToggle) {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                        )
                    }
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = Color.White,
            cursorColor = Color.White,
            selectionColors = TextSelectionColors(
                MediumGrey, MediumGrey
            )
        )
    )
}
