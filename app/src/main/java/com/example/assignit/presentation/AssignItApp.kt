package com.example.assignit.presentation

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assignit.presentation.starting_screen.HomeViewModel
import com.example.assignit.presentation.starting_screen.splash_screen.SplashScreen
import com.example.assignit.ui.theme.AssignItTheme
import com.example.assignit.ui.theme.DarkGrey
import com.example.assignit.ui.theme.DarkOrange
import com.example.assignit.util.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.presentation.sign_up_screen.SignUpScreen
import com.example.assignit.presentation.starting_screen.home_screen.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AssignItApp(
   homeViewModel: HomeViewModel = hiltViewModel()
) {
    AssignItTheme(
        dynamicColor = false
    ) {

        val appState = rememberAppState()

        Surface() {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarState,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData,
                                containerColor = DarkGrey,
                                contentColor = Color.White,
                                actionColor = DarkOrange,
                                dismissActionContentColor = Color.White,
                                modifier = Modifier.padding(8.dp),
                                actionOnNewLine = true
                            )
                        }
                    )
                }
            ) {
                NavHost(
                    navController = appState.navController,
//                    startDestination = SPLASH_SCREEN,
                    startDestination = SIGN_UP_SCREEN,
                ) {
                    taskAppGraph(
                        appState,
                        homeViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        TaskAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun NavGraphBuilder.taskAppGraph(
    appState: TaskAppState,
    homeViewModel: HomeViewModel
) {

    composable(SPLASH_SCREEN) {
        SplashScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            clearBackstack = { appState.clearBackstack() },
            viewModel = homeViewModel
        )
    }

    /*
    composable(LOGIN_SCREEN) {
        LoginScreen(

        )
    }
 */
    composable(SIGN_UP_SCREEN) {
        SignUpScreen(

        )
    }



    composable(HOME_SCREEN) {
        HomeScreen(
            viewModel = homeViewModel
        )
    }


    /*
    composable(SETTINGS_SCREEN){
        SettingsScreen(

        )
    }

     */


}
