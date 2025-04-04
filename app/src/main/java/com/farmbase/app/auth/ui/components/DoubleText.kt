package com.farmbase.app.auth.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DoubleText(
    modifier: Modifier = Modifier,
    @StringRes mainText: Int,
    @StringRes subText: Int,
) {
    Column(
        modifier = modifier.fillMaxWidth(),  // Ensures the column takes full width
        verticalArrangement = Arrangement.Center  // Keeps vertical arrangement unchanged
    ) {
        Text(
            text = stringResource(mainText),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Start,  // Align text to the start (left)
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .align(Alignment.Start)  // Ensures left alignment
        )
        Text(
            text = stringResource(subText),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .align(Alignment.Start)
        )
    }
}