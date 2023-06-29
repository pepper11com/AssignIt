package com.example.assignit.presentation.create_task_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.assignit.model.User
import com.example.assignit.ui.theme.DarkGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateCard(
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
    text: String,
    value: String,
){
    Card(
        modifier = modifier
            .fillMaxWidth().padding(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = DarkGrey,
        ),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = text)

            if (value.isNotEmpty()){
                Text(text = value)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAssigneeCard(
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
    text: String,
    assignees: List<User>
){
    Card(
        modifier = modifier
            .fillMaxWidth().padding(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = DarkGrey,
        ),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(text = text)

            if (assignees.isNotEmpty()){
                LazyRow(content = {
                    items(assignees.size){
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = assignees[it].username
                        )
                    }
                })
            }

        }
    }
}