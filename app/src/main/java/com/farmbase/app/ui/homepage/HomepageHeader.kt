package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.ClickableText
import com.farmbase.app.utils.Functions
import com.farmbase.app.utils.SharedPreferencesManager

@Composable
fun HomepageHeader(role: String, showDialog: Boolean, onDialogDismiss: () -> Unit, onTextClicked: () -> Unit) {
    val accessToken = SharedPreferencesManager(LocalContext.current).encryptedGet("accessToken")
    val userInfo = accessToken?.let { Functions.decodeTokenPayload(it) }
    Column {
        Text(
            text = stringResource(R.string.homepage_name, role),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        ClickableText(
            text = stringResource(R.string.homepage_desc),
            clickableText = stringResource(R.string.see_more)
        ) { onTextClicked() }

        if (showDialog) {
            HomepageSeeMoreDialog(onDismissRequest = onDialogDismiss)
        }

        Spacer(modifier = Modifier.height(16.dp))

        UserInfoCard(
            image = R.drawable.ic_launcher_foreground,
            userName = "${userInfo?.firstName?:""} ${userInfo?.lastName?:" "}",
            userId = userInfo?.roleIds?.firstOrNull()?:"",
            userRole = userInfo?.roles?.firstOrNull()?:"",
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.gray))
    }
}
