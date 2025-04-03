package com.farmbase.app.ui.confirmAction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.BackButton
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun ConfirmActionScreen(
    onBackButtonClicked: () -> Unit,
    onSelectAnotherClicked: () -> Unit,
    onContinueClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), onBackClick = onBackButtonClicked) },
        bottomBar = {
            Row(modifier = Modifier.padding(16.dp)) {
                BackButton(
                    onClick = onSelectAnotherClicked,
                    buttonText = stringResource(R.string.select_another),
                    modifier = Modifier.weight(1f),
                    shouldIconShow = false
                )
                Spacer(modifier = Modifier.width(20.dp))

                NextButton(
                    onClick = onContinueClicked,
                    enabled = true,
                    buttonText = stringResource(R.string.continue_),
                    modifier = Modifier.weight(1f)
                )
            }

        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)) {

            Text(
                text = stringResource(R.string.confirm_action),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.confirm_action_desc),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(80.dp))
            Image(painter = painterResource(R.drawable.ic_alert),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally))

        }
    }
}