package com.farmbase.app.auth.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.auth.ui.components.PinCreatedDialog
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpInputField
import com.farmbase.app.auth.ui.components.otp.OtpState
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import com.farmbase.app.utils.HashHelper

/*@Composable
fun LoginScreen(
    innerPadding: PaddingValues,
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    var dialogOpened by remember { mutableStateOf(false) }
    var userPinCreationSuccess by remember { mutableStateOf(false) }

//    Scaffold(
//
//        content = {
//            paddingValues ->

    // hashed otp code
//    viewModel.firstOtpCodeData = state.code.joinToString("")

    val otpCode =
        state.code.joinToString("") // Convert the list of digits to a string

//       val hashed4DigitCode = HashHelper.sha256(otpCode)

    // viewModel.firstOtpCodeData = otpCode
//          navController.navigate(Screen.OtpScreen2.createRoute(hashed4DigitCode))
    val hashed4DigitCode = HashHelper.sha256(otpCode)
    viewModel.firstOtpCodeData = hashed4DigitCode

    Column(
        modifier = modifier
//                    .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        DoubleText(mainText = R.string.enter_security_pin,
            subText = R.string.enter_security_pin_subtext)

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 250.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            onAction(OtpAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        onAction(OtpAction.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        onAction(OtpAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }

            state.isValid?.let { isValid ->
                LaunchedEffect(isValid) {
                    dialogOpened = true
                    userPinCreationSuccess = isValid
                }

                PinCreatedDialog(
                    dialogOpened = dialogOpened,
                    onNextClicked = {
                        // Handle what happens when Next is clicked
                    },
                    onDialogClosed = {
                        dialogOpened = false
                    },
                    userPinCreationSuccess = userPinCreationSuccess
                )
            }
        }

        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.cafitech_light_green)

            ),
            onClick = onClick,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(stringResource(R.string.next))
        }

        state.isValid?.let { isValid ->
            LaunchedEffect(isValid) {
                dialogOpened = true
                userPinCreationSuccess = isValid
            }
        }
    }

//        }
//    )

}*/

@Composable
fun LoginScreen(
    innerPadding: PaddingValues,
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    var dialogOpened by remember { mutableStateOf(false) }
    var userPinCreationSuccess by remember { mutableStateOf(false) }

    val otpCode = state.code.joinToString("") // Convert the list of digits to a string

    viewModel.firstOtpCodeData = otpCode

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        DoubleText(mainText = R.string.enter_security_pin,
            subText = R.string.enter_security_pin_subtext)

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 250.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            onAction(OtpAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        onAction(OtpAction.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        onAction(OtpAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }

            // Show Pin Created Dialog if OTP is valid
            state.isValid?.let { isValid ->
                LaunchedEffect(isValid) {
                    dialogOpened = true
                    userPinCreationSuccess = isValid
                }

                PinCreatedDialog(
                    dialogOpened = dialogOpened,
                    onNextClicked = {
                        // Handle what happens when Next is clicked
                    },
                    onDialogClosed = {
                        dialogOpened = false
                    },
                    userPinCreationSuccess = userPinCreationSuccess
                )
            }
        }

        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.cafitech_light_green)
            ),
            onClick = onClick,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(stringResource(R.string.next))
        }

        state.isValid?.let { isValid ->
            LaunchedEffect(isValid) {
                dialogOpened = true
                userPinCreationSuccess = isValid
            }
        }
    }
}


