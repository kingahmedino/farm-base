package com.farmbase.app.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R

@Composable
fun TopBar(modifier: Modifier, onBackClick: () -> Unit) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.align(Alignment.TopStart), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Localized description",
                    tint = colorResource(R.color.top_bar_icon_tint)

                )
            }

            /*Icon(
                modifier = Modifier.size(20.dp).clickable { onBackClick() },
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                tint = colorResource(R.color.top_bar_icon_tint)
            )*/

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = stringResource(R.string.back),
                style = MaterialTheme.typography.bodySmall, color = colorResource(R.color.gray))
        }

        Row(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                    contentDescription = "Speaker Image",
                    tint = colorResource(R.color.gray)

                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu Image",
                    tint = colorResource(R.color.top_bar_icon_tint)

                )
            }

        }
    }

}