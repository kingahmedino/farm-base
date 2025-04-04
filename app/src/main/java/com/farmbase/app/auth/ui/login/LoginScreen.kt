package com.farmbase.app.auth.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.auth.ui.components.PinCreatedDialog
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpInputField
import com.farmbase.app.auth.ui.components.otp.OtpState
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.auth.util.AuthObjects.launchForgotPasswordWebsite
import com.farmbase.app.auth.util.AuthObjects.launchWebsite
import com.farmbase.app.utils.HashHelper
import com.farmbase.app.utils.SharedPreferencesManager

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

    val context = LocalContext.current

    val savedHashedOtp = SharedPreferencesManager(context).encryptedGet(key = "userOtp")
//
    var otpCode = state.code.joinToString("")

    val hashedOtpCde = HashHelper.sha256(otpCode)


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Section
        DoubleText(
            mainText = R.string.enter_security_pin,
            subText = R.string.enter_security_pin_subtext
        )


        // Middle Section
        Column(
            modifier = Modifier.padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
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

                    if (hashedOtpCde == savedHashedOtp)
                    {
                        Toast.makeText(context, "Yessss", Toast.LENGTH_SHORT).show()

                    }
                    else {
                        Toast.makeText(context, "Nooo", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            Text(
                text = "Forgot Your Pin?",
                style = TextStyle(color = Color.Blue),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
//                        launchWebsite(context = context)
                        launchForgotPasswordWebsite(context = context)
                    }
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes the Button to the bottom

        // Bottom Section
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.cafitech_light_green)
            ),
            onClick = {


            },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(stringResource(R.string.next))
        }
    }
}



