package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.ClickableText

@Composable
fun HomepageHeader(role: String, showDialog: Boolean, onDialogDismiss: () -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.homepage_name, role),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        ClickableText(
            text = stringResource(R.string.homepage_desc),
            clickableText = stringResource(R.string.see_more)
        ) { onDialogDismiss() }

        if (showDialog) {
            HomepageSeeMoreDialog(onDismissRequest = onDialogDismiss)
        }

        Spacer(modifier = Modifier.height(16.dp))

        UserInfoCard(
            image = R.drawable.ic_placeholder_image,
            userName = "Ahmed Musa",
            userId = "T-1939u9u94",
            userRole = role,
            rfCount = 15
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.gray))
    }
}
