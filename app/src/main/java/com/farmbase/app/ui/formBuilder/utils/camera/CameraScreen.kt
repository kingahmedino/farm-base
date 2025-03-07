package com.farmbase.app.ui.formBuilder.utils.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.ui.formBuilder.utils.MediaInputButton

/**
 * This component displays a button that, when clicked, opens a dialog with camera functionality.
 * After the user captures media, the file is passed back through the onMediaCaptured callback.
 *
 * @param title Text label that appears above the button
 * @param type Determines the camera mode - "image" for photo capture or "video" for video recording
 * @param onMediaCaptured Callback that provides the URI and file path of the captured media
 */
@Composable
fun CameraScreen(
    title: String,
    type: String,
    viewModel: CameraViewModel = hiltViewModel(),
    onMediaCaptured: (Uri, String) -> Unit
) {
    var showCameraDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionsToRequest = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val allGranted = permissionsToRequest.all { perms[it] == true }
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(permission, perms[permission] == true)
            }
            if (allGranted) {
                showCameraDialog = true
            } else {
                Toast.makeText(context, "Camera and audio permissions are required", Toast.LENGTH_SHORT).show()
            }
        }

    Column {
        MediaInputButton(
            title = title,
            onClick = {
                if (permissionsToRequest.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
                    showCameraDialog = true
                } else {
                    permissionLauncher.launch(permissionsToRequest)
                }
            }
        )

        if (showCameraDialog) {
            DialogCameraScreen(
                type = type,
                onDismiss = { showCameraDialog = false },
                onMediaCaptured = { uri, filepath ->
                    onMediaCaptured(uri, filepath)
                    showCameraDialog = false
                }
            )
        }
    }
}


/**
 * This component creates a full-screen dialog containing the camera functionality.
 * It handles dismissal of the dialog and passes captured media back to the parent.
 *
 * @param type Determines the camera mode - "image" for photo capture or "video" for video recording
 * @param onDismiss Callback invoked when the user dismisses the camera dialog
 * @param onMediaCaptured Callback that provides the URI and file path of the captured media
 */
@Composable
fun DialogCameraScreen(
    type: String,
    onDismiss: () -> Unit,
    onMediaCaptured: (Uri, String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        CameraInput(
            captureType = type,
            onDismiss = onDismiss,
            onMediaCaptured = onMediaCaptured
        )
    }
}