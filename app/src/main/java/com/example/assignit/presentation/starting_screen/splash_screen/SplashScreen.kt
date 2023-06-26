package com.example.assignit.presentation.starting_screen.splash_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.presentation.starting_screen.HomeViewModel

@Composable
fun SplashScreen(
    openAndPopUp: (String, String) -> Unit,
    clearBackstack: () -> Unit,
    viewModel: HomeViewModel
) {

    LaunchedEffect(Unit){
        viewModel.onAppStart(openAndPopUp)
    }


    //todo make a column as big as the device with a loading indicator in the middle
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LoadingIndicator()

    }




}