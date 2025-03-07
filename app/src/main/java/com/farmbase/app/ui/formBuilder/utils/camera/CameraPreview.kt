package com.farmbase.app.ui.formBuilder.utils.camera

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * A composable that displays the camera preview feed.
 *
 * This component wraps the Android CameraX PreviewView in an AndroidView composable
 * to integrate it into the Jetpack Compose UI hierarchy. It displays the live camera feed
 * from the provided LifecycleCameraController.
 *
 * @param controller The LifecycleCameraController that manages the camera and its lifecycle
 * @param modifier Modifier to be applied to the composable
 */
@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = modifier.fillMaxSize()
    )
}