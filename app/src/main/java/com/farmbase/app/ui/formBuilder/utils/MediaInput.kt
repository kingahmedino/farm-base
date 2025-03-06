package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R

/**
 * A reusable button composable for media input actions.
 *
 * This composable displays a labeled button with an upload icon that can be used to trigger
 * various media input actions like capturing photos, recording videos, or selecting files.
 * It has a consistent appearance with transparent background and light gray border.
 *
 * @param modifier Modifier to be applied to the composable
 * @param title Text label that appears above the button
 * @param onClick Callback invoked when the button is clicked
 */
@Composable
fun MediaInputButton(
    modifier: Modifier = Modifier,
    title: String = "Upload 1 supported file. Max 5 MB.",
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Upload,
                contentDescription = "Upload Icon",
                tint = colorResource(R.color.hyper_text_link)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Add file",
                color = colorResource(R.color.hyper_text_link),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}