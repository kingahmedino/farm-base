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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.farmbase.app.ui.widgets.BottomSheet
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.utils.HashHelper
import com.farmbase.app.utils.SharedPreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
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
    var areOtpCodesTheSame by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val context = LocalContext.current

    val savedHashedOtp = SharedPreferencesManager(context).encryptedGet(key = "userOtp")
//
    var otpCode = state.code.joinToString("")

    val hashedOtpCde = HashHelper.sha256(otpCode)


    val headerText = if(areOtpCodesTheSame) stringResource(R.string.pin_correct) else stringResource(R.string.pin_mismatch)
    val decText = if(areOtpCodesTheSame) stringResource(R.string.pin_correct_subtext) else stringResource(R.string.pin_mismatch_subtext)
    val textColor = if(areOtpCodesTheSame) colorResource(R.color.black_text) else colorResource(R.color.cafitech_dark_red)
    val backgroundColor = if(areOtpCodesTheSame) colorResource(R.color.light_yellow) else colorResource(R.color.cafitech_light_red)
    val buttonColor = if(areOtpCodesTheSame) R.color.yellow else R.color.cafitech_dark_red
    val buttonTextColor = if(areOtpCodesTheSame) R.color.black_text else R.color.white
    val iconTint = if(areOtpCodesTheSame) R.color.black_text else R.color.white

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
        onButtonClick = { if(areOtpCodesTheSame) { onClick() } else { dialogOpened = false } }
    )


    Scaffold(modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()){ }
        },
        bottomBar = { NextButton(
            onClick = { onClick() } ,
            enabled = state.code.all {
//                it != null
                hashedOtpCde == savedHashedOtp
                                     },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) }
    ) {paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {

            DoubleText(mainText = R.string.enter_security_pin,
                subText = R.string.enter_security_pin_subtext)

            Spacer(modifier = Modifier.height(54.dp))

            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
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

//                state.isValid?.let { isValid ->
//
//                    if (hashedOtpCde == savedHashedOtp)
//                    {
//                        areOtpCodesTheSame = true
//                        Toast.makeText(context, "Yessss", Toast.LENGTH_SHORT).show()
//                    }
//                    else {
//                        areOtpCodesTheSame = false
//                        Toast.makeText(context, "Nooo", Toast.LENGTH_SHORT).show()
//                    }
//                }

//                state.isValid?.let { isValid ->
//                    if (isValid) {
//                        areOtpCodesTheSame = hashedOtpCde == savedHashedOtp
//                        dialogOpened = true
//                    }
//                }

                state.isValid?.let { isValid ->
                    LaunchedEffect(isValid) {
                        areOtpCodesTheSame = hashedOtpCde == savedHashedOtp
                        dialogOpened = true
                    }
                }


            }

            Text(
                text = "Forgot Your Pin?",
                style = TextStyle(color = Color.Blue),
                modifier = Modifier
                    .padding(top = 36.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
//                        launchWebsite(context = context)
                        launchForgotPasswordWebsite(context = context)
                    }
            )
        }

    }
}



