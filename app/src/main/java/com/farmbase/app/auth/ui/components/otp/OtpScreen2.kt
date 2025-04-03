package com.farmbase.app.auth.ui.components.otp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.auth.ui.components.PinCreatedDialog

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
//                    .padding(paddingValues)
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
    }

//        }
//    )


}
