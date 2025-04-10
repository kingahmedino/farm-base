package com.farmbase.app.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
fun BaseDialog(modifier: Modifier = Modifier, iconDrawable: Int, headerTextRes: Int?, subText: Int) {
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

        Image(painter = painterResource(iconDrawable),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center))
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