package com.farmbase.app.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.ui.theme.FarmBaseTheme

@Composable
fun EnterCommentTakePicture(modifier: Modifier = Modifier) {
    Column (modifier = modifier.fillMaxSize().background(Color.White)){
        DoubleText(
            mainText = R.string.enter_comments_take_picture,
            subText = R.string.enter_comments_take_picture_desc
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.enter_comment),
            style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text(
                text = "Enter details of the update here",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            ) },
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.gray),
                unfocusedBorderColor =colorResource(R.color.gray),
                unfocusedContainerColor = colorResource(R.color.light_gray),
                focusedContainerColor = colorResource(R.color.light_gray),
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        BackButton(onClick = {}, modifier = Modifier.fillMaxWidth(), buttonText = "Take Picture")

    }
}

@Preview
@Composable
fun EnterCommentTakePicturePrev() {
    FarmBaseTheme {
        EnterCommentTakePicture()
    }
}
