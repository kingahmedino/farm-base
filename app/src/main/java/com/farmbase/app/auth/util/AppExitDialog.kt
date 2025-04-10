package com.farmbase.app.auth.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun AppExitDialog(activity : ComponentActivity){

    val showExitDialog = remember { mutableStateOf(false) }

    // Back press handler
    BackHandler {
        showExitDialog.value = true
    }

    // Show exit confirmation dialog
    if (showExitDialog.value) {
        AlertDialog(
            onDismissRequest = { showExitDialog.value = false },
            title = { Text("Exit App") },
            text = { Text("Are you sure you want to close the app?") },
            confirmButton = {
                TextButton(onClick = { activity.finish() }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }
}