package com.farmbase.app.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.theme.FarmBaseTheme

@Composable
fun TopBar(modifier: Modifier, onBackClick: () -> Unit) {
    val context = LocalContext.current

    val menuItem = listOf(
        Pair(stringResource(R.string.settings) , Icons.Outlined.Settings),
        Pair(stringResource(R.string.notifications) , Icons.Outlined.Notifications),
        Pair(stringResource(R.string.sync) , Icons.Outlined.Sync)
    )
    var shouldMenuShow by remember { mutableStateOf(false) }

    Box(modifier = modifier.statusBarsPadding()) {
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
                    tint = colorResource(R.color.light_gray)

                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = {shouldMenuShow = true}) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu Image",
                    tint = colorResource(R.color.top_bar_icon_tint)

                )
            }

            if (shouldMenuShow) {
                Menu(expanded = true, menuItem = menuItem,
                    onClick = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                    onDismissRequest = {shouldMenuShow = false}
                )
            }

        }
    }

}

@Composable
@Preview
fun TopBarPreview(){
    FarmBaseTheme {
        TopBar(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){}
    }
}