package com.example.assignit.presentation.auth_screens.group_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.model.Group
import com.example.assignit.presentation.CREATE_TASK_SCREEN
import com.example.assignit.ui.theme.InvalidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    navigate: (String) -> Unit,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.group?.name ?: "Group")
                },
                navigationIcon = {
                    //TODO: Add the back button here
                },
                actions = {
                    IconButton(
                        onClick = {
                            val route = "$CREATE_TASK_SCREEN/${uiState.group?.id}"
                            navigate(route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                LoadingIndicator()
            }

            uiState.error?.let {
                Text(text = it, color = InvalidColor)
            }

            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when  {
                    uiState.group != null -> {



                        LazyRow(

                        ){
                            //TODO: ADD THE LIST OF TASKS HERE

                        }




                        LazyColumn(

                        ){

                            //TODO: Add the list of members here

                            items(uiState.group!!.members.size) { member ->
                                Text(text = uiState.group!!.members[member].username)
                            }


                        }
                    }
                }
            }
        }
    }
}