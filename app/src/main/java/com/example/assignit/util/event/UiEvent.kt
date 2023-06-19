package com.example.assignit.util.event

sealed class UiEvent{
    data class ShowSnackBar(val message: String, val action: String? = null): UiEvent()
}
