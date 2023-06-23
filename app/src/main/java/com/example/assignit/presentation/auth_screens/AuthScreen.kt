package com.example.assignit.presentation.auth_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.assignit.common.composables.CustomTabRow
import com.example.assignit.presentation.auth_screens.login_screen.LoginScreen
import com.example.assignit.presentation.auth_screens.sign_up_screen.SignUpScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(

) {


    val tabTitles = listOf("Login", "Register")
    val pagerState = rememberPagerState(0, 0F) { tabTitles.size }
    val selectedIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {

            CustomTabRow(
                selectedIndex = selectedIndex,
                tabTitles = tabTitles,
                pagerState = pagerState,
                coroutineScope = coroutineScope
            )

//            TabRow(selectedTabIndex = selectedIndex) {
//                tabTitles.forEachIndexed { index, title ->
//                    Tab(
//                        text = { Text(title) },
//                        selected = selectedIndex == index,
//                        onClick = {
//                            coroutineScope.launch {
//                                pagerState.animateScrollToPage(index)
//                            }
//                        }
//                    )
//                }
//            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> {
                    LoginScreen()
                }
                1 -> {
                    SignUpScreen()
                }
            }
        }
    }
}