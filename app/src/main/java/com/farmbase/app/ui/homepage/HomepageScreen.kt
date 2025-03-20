package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.theme.FarmBaseTheme
import com.farmbase.app.ui.widgets.ClickableText
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun Homepage() {
    // todo: fetch user data, figure button enabled and disabled state
    Scaffold(modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()){}
        },
        bottomBar = { NextButton(
            onClick = {},
            enabled = true,
            modifier = Modifier.fillMaxWidth().padding(16.dp)) }
    ) {paddingValues ->
        Column ( modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(text = stringResource(R.string.homepage_name, "Poultry Hub Lead"), style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(12.dp))
            ClickableText(text = stringResource(R.string.homepage_desc), clickableText = stringResource(R.string.see_more)) { }
            Spacer(modifier = Modifier.height(16.dp))
            UserInfoCard(
                image = R.drawable.ic_launcher_foreground,
                userName = "Aderinola Bankole",
                userId = "T-1939u9u94",
                userRole = "Member Success Coach",
                rfCount = 15
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    FarmBaseTheme {
        Homepage()
    }
}