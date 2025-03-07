package com.farmbase.app.ui.formBuilder.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Check if all required permissions are granted
fun hasRequiredCameraPermissions(context: Context): Boolean {
    return CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

// Take photo and save to file
fun takePhoto(
    controller: CameraController,
    context: Context,
    onFileSaved: (Uri, String) -> Unit
) {
    if (!hasRequiredCameraPermissions(context)) {
        return
    }

    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }

                val bitmap = image.toBitmap()
                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )

                val file = saveImageToFile(rotatedBitmap, context)
                file?.let {
                    onFileSaved(it.absolutePath.toUri(), file.name)
                    Toast.makeText(
                        context,
                        "Image saved to: ${it.absolutePath}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Toast.makeText(
                    context,
                    "Failed to capture image: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}

// Save bitmap to file with timestamp
fun saveImageToFile(bitmap: Bitmap, context: Context): File? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val filename = "IMG_${timeStamp}.jpg"
    val file = File(context.filesDir, filename)

    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
        }
        return file
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(
            context,
            "Failed to save image: ${e.message}",
            Toast.LENGTH_SHORT
        ).show()
        return null
    }
}

// Start or stop video recording
@SuppressLint("MissingPermission")
fun recordVideo(
    controller: LifecycleCameraController,
    currentRecording: Recording?,
    context: Context,
    onFileSaved: (Uri, String) -> Unit
): Recording? {
    // If already recording, stop and return null
    if (currentRecording != null) {
        currentRecording.stop()
        return null
    }

    if (!hasRequiredCameraPermissions(context)) {
        return null
    }

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val filename = "VID_${timeStamp}.mp4"
    val outputFile = File(context.filesDir, filename)

    return controller.startRecording(
        FileOutputOptions.Builder(outputFile).build(),
        AudioConfig.create(true),
        ContextCompat.getMainExecutor(context)
    ) { event ->
        when (event) {
            is VideoRecordEvent.Finalize -> {
                if (event.hasError()) {
                    Toast.makeText(
                        context,
                        "Video capture failed: ${event.error}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    onFileSaved(outputFile.absolutePath.toUri(), filename)
                    Toast.makeText(
                        context,
                        "Video saved to: ${outputFile.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

// Get standard camera permissions
val CAMERAX_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)