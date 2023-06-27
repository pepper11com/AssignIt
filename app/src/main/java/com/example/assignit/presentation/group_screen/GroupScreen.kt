package com.example.assignit.presentation.group_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.assignit.common.composables.LoadingIndicator
import com.example.assignit.model.Group
import com.example.assignit.presentation.GROUP_DETAIL_SCREEN
import com.example.assignit.ui.theme.DarkGrey
import com.example.assignit.ui.theme.DarkOrange
import com.example.assignit.ui.theme.InvalidColor
import com.example.assignit.ui.theme.MediumGrey

@Composable
fun GroupScreen(
    openAndPopUp: (String, String) -> Unit,
    navigate: (String) -> Unit,
    viewModel: GroupViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ){
        if (uiState.isLoading) {
            LoadingIndicator()
        }

        uiState.error?.let {
            Text(text = it, color = InvalidColor)
        }

        //TODO list of groups ( cards ) with the group name and the group members

        LazyColumn (
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize(),
        ){
            items(uiState.groups) { group ->
                GroupCard(group, navigate)
            }
        }
    }
}

@Composable
fun GroupCard(
    group: Group,
    navigate: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Text(
                    text = "Members: ${group.members.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom
            ) {

                Button(
                    onClick = { navigate(GROUP_DETAIL_SCREEN + "/${group.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkOrange),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Open Group",
                        color = Color.White
                    )
                }


            }

        }
    }
}

