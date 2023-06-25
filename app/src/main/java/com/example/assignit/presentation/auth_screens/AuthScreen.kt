package com.example.assignit.presentation.auth_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.assignit.R
import com.example.assignit.common.composables.CustomTabRow
import com.example.assignit.presentation.auth_screens.login_screen.LoginScreen
import com.example.assignit.presentation.auth_screens.sign_up_screen.SignUpScreen
import com.example.assignit.ui.theme.DarkOrange

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(

) {

    val tabTitles = listOf("Login", "Register")
    val pagerState = rememberPagerState(0, 0F) { tabTitles.size }
    val selectedIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(pagerState.currentPage) {
        focusManager.clearFocus()
    }

    Scaffold(
        topBar = {
            Column {
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

                CustomTabRow(
                    selectedIndex = selectedIndex,
                    tabTitles = tabTitles,
                    pagerState = pagerState,
                    coroutineScope = coroutineScope,
                    focusManager = focusManager
                )
            }



        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> {

                }
                1 -> {

                }
            }
        }
    }
}