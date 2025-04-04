package com.farmbase.app.auth.ui.components.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.ui.widgets.BottomSheet
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen2(
    state: OtpState,
    otpCode: String,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    onClick: () -> Unit,
    onBackButtonClicked: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    var dialogOpened by remember { mutableStateOf(false) }
    var userPinCreationSuccess by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()


    val headerText = if(userPinCreationSuccess) stringResource(R.string.pin_created_successfully) else stringResource(R.string.pin_mismatch)
    val decText = if(userPinCreationSuccess) stringResource(R.string.pin_created_successfully_subtext) else stringResource(R.string.pin_mismatch_subtext)
    val textColor = if(userPinCreationSuccess) colorResource(R.color.black_text) else colorResource(R.color.cafitech_dark_red)
    val backgroundColor = if(userPinCreationSuccess) colorResource(R.color.light_yellow) else colorResource(R.color.cafitech_light_red)
    val buttonColor = if(userPinCreationSuccess) R.color.yellow else R.color.cafitech_dark_red
    val buttonTextColor = if(userPinCreationSuccess) R.color.black_text else R.color.white
    val iconTint = if(userPinCreationSuccess) R.color.black_text else R.color.white

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
        onButtonClick = { if(userPinCreationSuccess) { onClick() } else { dialogOpened = false } }
    )


    val context = LocalContext.current

    // hashed otp code
    viewModel.firstOtpCodeData = otpCode

//    Toast.makeText(context, "${otpCode}", Toast.LENGTH_SHORT).show()

//    Scaffold(
//        content = {paddingValues ->

    Scaffold(modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()){ onBackButtonClicked() }
        },
        bottomBar = { NextButton(
            onClick = { dialogOpened = true } ,
            enabled = state.code.all { it != null },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) }
    ) {paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            DoubleText(
                mainText = R.string.confirm_security_pin,
                subText = R.string.confirm_your_4_digit_security_pin_to_proceed
            )

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
                }
            }

            state.isValid?.let { isValid ->
                LaunchedEffect(isValid) {
                    userPinCreationSuccess = isValid
                }
            }
        }
    }
}




