package com.farmbase.app.auth.ui.login

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpInputField
import com.farmbase.app.auth.ui.components.otp.OtpState
import com.farmbase.app.auth.util.AuthObjects.launchForgotPasswordWebsite
import com.farmbase.app.ui.widgets.BottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    innerPadding: PaddingValues,
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    context: Context,
    savedHashedOtp : String?,
    hashedOtpCde : String,
) {
    var dialogOpened by remember { mutableStateOf(false) }
    var areOtpCodesTheSame by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val headerText =
        if (areOtpCodesTheSame) stringResource(R.string.pin_correct) else stringResource(R.string.pin_mismatch)
    val decText =
        if (areOtpCodesTheSame) stringResource(R.string.pin_correct_subtext) else stringResource(R.string.pin_mismatch_subtext)
    val textColor =
        if (areOtpCodesTheSame) colorResource(R.color.black_text) else colorResource(R.color.cafitech_dark_red)
    val backgroundColor =
        if (areOtpCodesTheSame) colorResource(R.color.light_yellow) else colorResource(R.color.cafitech_light_red)
    val buttonColor = if (areOtpCodesTheSame) R.color.yellow else R.color.cafitech_dark_red
    val buttonTextColor = if (areOtpCodesTheSame) R.color.black_text else R.color.white
    val iconTint = if (areOtpCodesTheSame) R.color.black_text else R.color.white

    BottomSheet(
        sheetState = sheetState,
        showBottomSheet = dialogOpened,
        sheetColor = backgroundColor,
        headerText = headerText,
        descText = decText,
        textColor = textColor,
        buttonColor = buttonColor,
        buttonTextColor = buttonTextColor,
        iconTint = iconTint,
        onDismissRequest = { dialogOpened = false },
        onButtonClick = {
            if (areOtpCodesTheSame) {
                onClick()
                dialogOpened = false


            } else {
                dialogOpened = false
            }
        }
    )


    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {

        DoubleText(
            mainText = R.string.enter_security_pin,
            subText = R.string.enter_security_pin_subtext
        )

        Spacer(modifier = Modifier.height(54.dp))

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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
                        .padding(8.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }

            state.isValid?.let { isValid ->
                LaunchedEffect(isValid) {
                    areOtpCodesTheSame = hashedOtpCde == savedHashedOtp
                    dialogOpened = true
                }
            }


        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Forgot Your Pin?",
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.otp_color),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    launchForgotPasswordWebsite(context = context)
                }
        )
    }

}