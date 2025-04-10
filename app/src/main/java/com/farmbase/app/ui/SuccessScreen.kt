package com.farmbase.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.theme.FarmBaseTheme
import com.farmbase.app.ui.widgets.BaseDialog
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun SuccessScreen(
    onNextButtonClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier,
        containerColor = colorResource(R.color.yellow),
        topBar = { TopBar(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background), onBackClick = {}) },
        bottomBar = {
            NextButton(
                onClick = onNextButtonClicked,
                enabled = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    ) { paddingValues ->

        BaseDialog(
            modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp),
            iconDrawable = R.drawable.ic_done,
            headerTextRes = null,
            subText =  R.string.confirm_action_desc,
            iconText = stringResource(R.string.done)
        )
    }
}

@Preview
@Composable
fun Scussee() {
    FarmBaseTheme {
        SuccessScreen { }
    }
}