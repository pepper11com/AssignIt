package com.example.assignit.util.snackbar

import com.example.assignit.util.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ActionSnackbarManager @Inject constructor() {
    private val _snackbarMessages = MutableStateFlow<UiEvent?>(null)
    val snackbarMessages: StateFlow<UiEvent?> get() = _snackbarMessages

    fun post(message: UiEvent) {
        _snackbarMessages.value = message
    }

    fun clear() {
        _snackbarMessages.value = null
    }
}
