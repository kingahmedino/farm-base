package com.farmbase.app.ui.formBuilder.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.farmbase.app.ui.theme.FarmBaseTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A composable that provides a file upload button for images or videos.
 *
 * This component displays a button that, when clicked, opens the system's visual media picker.
 * It allows users to select either images or videos based on the specified type.
 * After a file is selected, its URI and a generated filename are passed to the onClick callback.
 *
 * @param modifier Modifier to be applied to the composable
 * @param title Text label that appears above the button
 * @param type The type of file to select ("image" or "video")
 * @param onClick Callback that provides the selected file's URI and generated filename
 */
@Composable
fun UploadFile(
    modifier: Modifier = Modifier,
    title: String = "Upload 1 supported file: image. Max 5 MB.",
    type: String,
    onClick: (Uri, String) -> Unit = { _, _ -> }
) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = generateFileName(type)
            onClick(it, fileName)
        }
    }

    MediaInputButton(
        modifier = modifier,
        title = title,
        onClick = {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(
                    if (type == "image") ActivityResultContracts.PickVisualMedia.ImageOnly
                    else ActivityResultContracts.PickVisualMedia.VideoOnly
                )
            )
        }
    )
}

/**
 * Generates a unique filename for uploaded files based on the current timestamp.
 *
 * @param type The type of file ("image" or "video") to determine file extension
 * @return A unique filename with the appropriate extension
 */
fun generateFileName(type: String): String {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val extension = if (type == "image") ".jpg" else ".mp4"

    return "file_${timeStamp}$extension"
}

/**
 * Preview composable for the UploadFile component.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UploadFilePreview() {
    FarmBaseTheme {
        UploadFile(type = "image")
    }
}