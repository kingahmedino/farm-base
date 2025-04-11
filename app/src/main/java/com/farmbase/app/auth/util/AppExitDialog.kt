package com.farmbase.app.auth.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.farmbase.app.R

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
            title = { Text(stringResource(R.string.exit_app)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_close_the_app)) },
            confirmButton = {
                TextButton(onClick = { activity.finish() }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog.value = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}