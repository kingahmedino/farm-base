package com.farmbase.app.auth.ui.components.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.auth.ui.components.PinCreatedDialog
import com.farmbase.app.ui.widgets.NextButton

@Composable
fun OtpScreen2(
    state: OtpState,
    otpCode: String,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    var dialogOpened by remember { mutableStateOf(false) }
    var userPinCreationSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // hashed otp code
    viewModel.firstOtpCodeData = otpCode

//    Toast.makeText(context, "${otpCode}", Toast.LENGTH_SHORT).show()

//    Scaffold(
//        content = {paddingValues ->

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        DoubleText(
            mainText = R.string.confirm_security_pin,
            subText = R.string.confirm_your_4_digit_security_pin_to_proceed
        )

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 250.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Spacer(modifier = Modifier.size(8.dp))
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
                Spacer(modifier = Modifier.size(8.dp))
            }

            state.isValid?.let { isValid ->
                LaunchedEffect(isValid) {
                    dialogOpened = true
                    userPinCreationSuccess = isValid
                }

                PinCreatedDialog(
                    dialogOpened = dialogOpened,
                    onNextClicked = { isSuccessful ->
                        // Handle what happens when Next is clicked
                        if (isSuccessful) {
                            onClick()
                        }
                    },
                    onDialogClosed = {
                        dialogOpened = false
                    },
                    userPinCreationSuccess = userPinCreationSuccess
                )
            }
        }

        NextButton(
            onClick = {},
            enabled = state.code.all { it != null },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }

//        }
//    )


}
