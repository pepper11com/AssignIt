package com.example.assignit.presentation

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import com.example.assignit.util.snackbar.SnackbarManager
import com.example.assignit.util.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class TaskAppState(
    val snackbarState: SnackbarHostState,
    val navController: NavHostController,
    val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                snackbarState.showSnackbar(
                    text,
                    withDismissAction = true,
                )
                snackbarManager.messages.value = null
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    fun clearAndNavigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndPopUpMultiple(route: String, popUpScreens: List<String>) {
        navController.navigate(route) {
            launchSingleTop = true
            for (screen in popUpScreens) {
                popUpTo(screen) { inclusive = true }
            }
        }
    }
    fun clearBackstack() {
        navController.popBackStack()
    }
}