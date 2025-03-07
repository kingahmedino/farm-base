package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable for displaying a selected file as a tag with remove functionality.
 *
 * This component shows a horizontal item that contains a file icon (based on the file type),
 * the filename, and a close button to remove the file selection.
 *
 * @param type The type of file being displayed ("image", "video", or other)
 * @param title The display name or title of the file
 * @param onClose Callback invoked when the user clicks the close button to remove this file
 */
@Composable
fun FileTagItem(
    type: String = "",
    title: String,
    onClose: () -> Unit
) {
    val imageVector = when (type.lowercase()) {
        "image" -> Icons.Default.Image
        "video" -> Icons.Default.VideoFile
        else -> Icons.Default.Image
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "File Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onClose
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
    }
}

/**
 * Preview composable for the FileTagItem.
 *
 * Demonstrates how a video file tag would appear in the UI.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFileTagItem() {
    FileTagItem(
        type = "video",
        title = "Who We Are - No...",
        onClose = {}
    )
}