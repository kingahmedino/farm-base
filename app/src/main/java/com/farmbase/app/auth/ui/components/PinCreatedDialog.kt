package com.farmbase.app.auth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.farmbase.app.R
import com.farmbase.app.ui.theme.bgDarkYellow
import com.farmbase.app.ui.theme.bgLightYellow

data class AlertDialogInfo(
    val columnBackground: Color,
    val text: Color,
    val button: Color,
    val buttonText: Color,
    val dialogText: String,
    val dialogSubText: String
)

@Composable
fun BottomAlignedDialog(
    modifier: Modifier = Modifier,
    dialogOpened: Boolean,
    onNextClicked: () -> Unit,
    onDialogClosed: () -> Unit,
    userPinCreationSuccess: Boolean
) {
    if (dialogOpened) {
        Dialog(onDismissRequest = onDialogClosed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    , contentAlignment = Alignment.BottomCenter
                 //   .align(Alignment.BottomCenter) // Align to bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (userPinCreationSuccess) bgLightYellow
                            else colorResource(R.color.cafitech_light_red)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (userPinCreationSuccess)
                            stringResource(R.string.pin_created_successfully)
                        else stringResource(R.string.pin_mismatch),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (userPinCreationSuccess)
                            stringResource(R.string.pin_created_successfully_subtext)
                        else stringResource(R.string.pin_mismatch_subtext),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (userPinCreationSuccess) onNextClicked()
                            onDialogClosed()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = bgDarkYellow)
                    ) {
                        Text(text = stringResource(R.string.next), color = Color.Black)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    dialogOpened: Boolean,
    onNextClicked: () -> Unit,
    onDialogClosed: () -> Unit,
    userPinCreationSuccess: Boolean
) {
    if (dialogOpened) {

        val colors = if (userPinCreationSuccess) {
            AlertDialogInfo(
                columnBackground = bgLightYellow,
                text = Color.Black,
                button = bgDarkYellow,
                buttonText = Color.Black,
                dialogText = stringResource(R.string.pin_created_successfully),
                dialogSubText = stringResource(R.string.pin_created_successfully_subtext)
            )
        } else {
            AlertDialogInfo(
                columnBackground = colorResource(R.color.cafitech_light_red),
                text = colorResource(R.color.cafitech_dark_red),
                button = colorResource(R.color.cafitech_dark_red),
                buttonText = Color.White,
                dialogText = stringResource(R.string.pin_mismatch),
                dialogSubText = stringResource(R.string.pin_mismatch_subtext)
            )
        }

        ModalBottomSheet(
            modifier = Modifier,
            onDismissRequest = onDialogClosed,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Text(
                    text = colors.dialogText,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colors.text
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = colors.dialogSubText,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal,
                    color = colors.text
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (userPinCreationSuccess) onNextClicked()
                        onDialogClosed()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = bgDarkYellow)
                ) {
                    Text(text = stringResource(R.string.next), color = Color.Black)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


/*@Composable
fun PinCreatedDialog(
    dialogOpened: Boolean,
    onNextClicked: () -> Unit,
    onDialogClosed: () -> Unit,
    userPinCreationSuccess: Boolean
) {
    if (dialogOpened) {
        val colors = if (userPinCreationSuccess) {
            AlertDialogInfo(
                columnBackground = bgLightYellow,
                text = Color.Black,
                button = bgDarkYellow,
                buttonText = Color.Black,
                dialogText = stringResource(R.string.pin_created_successfully),
                dialogSubText = stringResource(R.string.pin_created_successfully_subtext)
            )
        } else {
            AlertDialogInfo(
                columnBackground = colorResource(R.color.cafitech_light_red),
                text = colorResource(R.color.cafitech_dark_red),
                button = colorResource(R.color.cafitech_dark_red),
                buttonText = Color.White,
                dialogText = stringResource(R.string.pin_mismatch),
                dialogSubText = stringResource(R.string.pin_mismatch_subtext)
            )
        }

        AlertDialog(
            onDismissRequest = { onDialogClosed() }, // Ensure the dialog closes
            containerColor = colors.columnBackground,
            title = {
                Text(
                    text = colors.dialogText,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colors.text
                )
            },
            text = {
                Text(
                    text = colors.dialogSubText,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal,
                    color = colors.text
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onNextClicked()
                        onDialogClosed() // Ensure the dialog actually closes
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.button),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.next),
                            color = colors.buttonText,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = colors.buttonText
                        )
                    }
                }
            }
        )
    }
}*/

@Composable
fun PinCreatedDialog(
    dialogOpened: Boolean,
    onNextClicked: (Boolean) -> Unit,
    onDialogClosed: () -> Unit,
    userPinCreationSuccess: Boolean
) {
    if (dialogOpened) {
        val colors = if (userPinCreationSuccess) {
            AlertDialogInfo(
                columnBackground = bgLightYellow,
                text = Color.Black,
                button = bgDarkYellow,
                buttonText = Color.Black,
                dialogText = stringResource(R.string.pin_created_successfully),
                dialogSubText = stringResource(R.string.pin_created_successfully_subtext)
            )
        } else {
            AlertDialogInfo(
                columnBackground = colorResource(R.color.cafitech_light_red),
                text = colorResource(R.color.cafitech_dark_red),
                button = colorResource(R.color.cafitech_dark_red),
                buttonText = Color.White,
                dialogText = stringResource(R.string.pin_mismatch),
                dialogSubText = stringResource(R.string.pin_mismatch_subtext)
            )
        }

        Dialog(onDismissRequest = onDialogClosed) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter // Moves the dialog content to the bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.columnBackground, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = colors.dialogText,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = colors.text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = colors.dialogSubText,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal,
                        color = colors.text
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onNextClicked(userPinCreationSuccess)
                            onDialogClosed()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.button),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.next),
                                color = colors.buttonText,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = colors.buttonText
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PinCreatedDialogPreview() {
    PinCreatedDialog(
        dialogOpened = true,
        onNextClicked = {},
        onDialogClosed = {},
        userPinCreationSuccess = true

    )
}
