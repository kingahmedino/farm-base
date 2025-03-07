package com.farmbase.app.ui.formBuilder.utils.camera

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.Recording
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.farmbase.app.ui.formBuilder.utils.recordVideo
import com.farmbase.app.ui.formBuilder.utils.takePhoto

/**
 * A composable that provides camera functionality for capturing photos or videos.
 *
 * This component implements a camera interface with controls for switching between front and back cameras,
 * capturing photos, or recording videos depending on the captureType parameter.
 *
 * @param captureType Determines the camera mode - "image" for photo capture or "video" for video recording
 * @param modifier Modifier to be applied to the composable
 * @param onDismiss Callback invoked when the user dismisses the camera
 * @param onMediaCaptured Callback that provides the URI and file path of the captured media
 */
@Composable
fun CameraInput(
    captureType: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onMediaCaptured: (Uri, String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember(lifecycleOwner) {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
            // Bind immediately to lifecycle
            bindToLifecycle(lifecycleOwner)
        }
    }

    // Bind to lifecycle
//    LaunchedEffect(controller) {
//        controller.bindToLifecycle(lifecycleOwner)
//    }

    var recording by remember { mutableStateOf<Recording?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            recording?.stop()
            controller.unbind()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())

        IconButton(
            onClick = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
                        CameraSelector.DEFAULT_BACK_CAMERA
                    else CameraSelector.DEFAULT_FRONT_CAMERA
            },
            modifier = Modifier.offset(16.dp, 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FlipCameraAndroid,
                contentDescription = "Switch camera",
                tint = Color.White
            )
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        // Capture Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            if (captureType == "image") {
                IconButton(
                    onClick = {
                        takePhoto(controller, context, onMediaCaptured)
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White.copy(alpha = 0.5f), CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Take Photo",
                        tint = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else if (captureType == "video") {
                IconButton(
                    onClick = {
                        recording = recordVideo(controller, recording, context, onMediaCaptured)
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            if (recording == null) Color.White.copy(alpha = 0.5f)
                            else Color.Red.copy(alpha = 0.7f),
                            CircleShape
                        )
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (recording == null)
                            Icons.Default.Videocam
                        else
                            Icons.Default.StopCircle,
                        contentDescription = if (recording == null) "Record Video" else "Stop Recording",
                        tint = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}