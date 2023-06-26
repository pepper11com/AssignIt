package com.example.assignit.presentation.starting_screen.home_screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.assignit.presentation.starting_screen.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    viewModel: HomeViewModel
){
    val groupName = remember { mutableStateOf(TextFieldValue()) }
    val currentGroupId = viewModel.currentGroupId.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Home Screen")

        OutlinedTextField(
            value = groupName.value,
            onValueChange = { groupName.value = it },
            label = { Text("Group Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Button(
            onClick = { viewModel.createGroup(groupName.value.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Create Group")
        }

        currentGroupId.value?.let { groupId ->
            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Invite Link", "https://assign-it-wesbite.vercel.app/deeplink/$groupId")
                    clipboard.setPrimaryClip(clip)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Copy Invite Link")
            }
        }
    }
}

