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
import androidx.compose.ui.text.font.FontWeight
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
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,  // Align text to the start (left)
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .align(Alignment.Start)  // Ensures left alignment
        )
        Text(
            text = stringResource(subText),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .align(Alignment.Start)
        )
    }
}