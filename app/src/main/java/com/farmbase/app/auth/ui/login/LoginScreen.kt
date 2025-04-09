package com.farmbase.app.auth.ui.login

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpState
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.ui.navigation.Screen
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.utils.Constants
import com.farmbase.app.utils.HashHelper
import com.farmbase.app.utils.SharedPreferencesManager

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: OtpViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val focusRequesters = remember { List(4) { FocusRequester() } }

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    val onClick: () -> Unit = {

        val otpCode =
            state.code.joinToString("") // Convert the list of digits to a string

//                            val hashed4DigitCode = HashHelper.sha256(otpCode)

        // viewModel.firstOtpCodeData = otpCode
//                            navController.navigate(Screen.OtpScreen2.createRoute(hashed4DigitCode))
        val hashed4DigitCode = HashHelper.sha256(otpCode)

        val programId =
            SharedPreferencesManager(context).encryptedGet(key = Constants.SELECTED_PROGRAM_ID)
        Log.d("Program Id", programId.toString())

        // save access and refresh token in encrypted shared prefs

        // navigate
        if (programId.isNullOrBlank()) {
            navController.navigate(Screen.SelectProgram.route)
        } else {
            navController.navigate(Screen.ConfirmAction.route)
        }
    }

    var otpCode = state.code.joinToString("")

    val savedHashedOtp = SharedPreferencesManager(context).encryptedGet(key = "userOtp")
    val hashedOtpCde = HashHelper.sha256(otpCode)


    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }


    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()) { }
        },
        bottomBar = {
            NextButton(
                onClick = { onClick() },
                enabled = state.code.all {
                    hashedOtpCde == savedHashedOtp
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        content = { paddingValues ->

            LoginScreenContent(
                savedHashedOtp = savedHashedOtp,
                hashedOtpCde = hashedOtpCde,
                innerPadding = paddingValues,
                state = OtpState(
                    code = listOf(),
                    focusedIndex = null,
                    isValid = null
                ),
                focusRequesters = focusRequesters,
                onAction = { action ->

                    when (action) {
                        is OtpAction.OnEnterNumber -> {
                            if (action.number != null) {
                                focusRequesters[action.index].freeFocus()
                            }
                        }

                        else -> Unit
                    }
                    viewModel.onAction(action)
                },
                modifier = modifier,
                onClick = onClick,
                context = context

            )
        }
    )



}



