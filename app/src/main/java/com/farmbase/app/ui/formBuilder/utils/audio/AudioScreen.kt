package com.farmbase.app.ui.formBuilder.utils.audio

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.farmbase.app.ui.formBuilder.utils.MediaInputButton
import com.farmbase.app.ui.formBuilder.utils.audio.AudioRecord

/**
 * This component displays a button, when clicked, opens a dialog with audio recording functionality.
 * After the user completes recording, the captured audio file is passed back through the onClick callback.
 *
 * @param modifier Modifier to be applied to the composable
 * @param title Text label that appears above the button
 * @param onClick Callback that provides the URI and file path of the captured audio
 */
@Composable
fun AudioScreen(
    modifier: Modifier = Modifier,
    title: String = "Upload 1 supported file: audio. Max 5 MB.",
    onClick: (Uri, String) -> Unit = { _, _ -> }
) {
    var showAudioDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showAudioDialog = true
        } else {
            Toast.makeText(context, "Microphone permission is required.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = modifier) {
        MediaInputButton(
            modifier = modifier,
            title = title,
            onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    showAudioDialog = true
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        )

        if (showAudioDialog) {
            Dialog(
                onDismissRequest = { showAudioDialog = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AudioRecord(
                        onDismiss = { showAudioDialog = false },
                        onAudioCaptured = { uri, path ->
                            onClick(uri, path)
                            showAudioDialog = false
                        }
                    )
                }
            }
        }
    }
}
