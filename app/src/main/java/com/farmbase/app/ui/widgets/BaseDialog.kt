package com.farmbase.app.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.ui.theme.FarmBaseTheme

@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    headerTextRes: Int?,
    subText: Int,
    iconDrawable: Int,
    iconText: String? = null
) {
    Box(modifier = modifier
        .fillMaxSize()
    ) {
        headerTextRes?.let {
            DoubleText(
                modifier = Modifier.align(Alignment.TopStart),
                mainText = it,
                subText = subText

            )
        }

        Column(modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(iconDrawable),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp))

            iconText?.let {
                Spacer(Modifier.height(12.dp))
                Text(text = it, style = MaterialTheme.typography.labelLarge)
            }
        }

    }
}

@Composable
@Preview
fun BaseDialogPreview() {
    FarmBaseTheme {
        BaseDialog(iconDrawable = R.drawable.ic_alert,
            headerTextRes = R.string.confirm_action,
            subText =  R.string.confirm_action_desc
        )
    }
}