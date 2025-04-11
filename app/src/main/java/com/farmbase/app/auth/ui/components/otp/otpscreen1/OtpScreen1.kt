package com.farmbase.app.auth.ui.components.otp.otpscreen1

import android.widget.Toast
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.ui.navigation.target.NavigationAuth
//import com.farmbase.app.ui.navigation.target.Screens
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.utils.HashHelper

@Composable
fun OtpScreen1(
    navController: NavController,
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    args: NavigationAuth.OtpScreen1?
) {

    // otp
    val viewModel: OtpViewModel = hiltViewModel(navBackStackEntry) // Retain ViewModel

    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember {
        List(4) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

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

    val status = args?.status ?: "N/A"
    val accessToken = args?.accessToken ?: "N/A"
    val refreshToken = args?.refreshToken ?: "N/A"
    val resetPin = args?.resetPin ?: false

//    val status = navBackStackEntry.arguments?.getString("status") ?: "N/A"
//    val accessToken = navBackStackEntry.arguments?.getString("accessToken") ?: "N/A"
//    val refreshToken = navBackStackEntry.arguments?.getString("refreshToken") ?: "N/A"
//    val resetPin = navBackStackEntry.arguments?.getBoolean("resetPin") ?: false


    val onClick: () -> Unit = {
        val otpCode =
            state.code.joinToString("") // Convert the list of digits to a string

        Toast.makeText(context, "$status $accessToken", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "$refreshToken $resetPin", Toast.LENGTH_LONG).show()

        val hashed4DigitCode = HashHelper.sha256(otpCode)
        navController.navigate(


            NavigationAuth.OtpScreen2(
                otpCode = hashed4DigitCode,
                accessToken = accessToken,
                refreshToken = refreshToken,
                resetPin = resetPin
            )

//            Screen.OtpScreen2.createRoute(
//                hashed4DigitCode,
//                accessToken,
//                refreshToken,
//                resetPin
//            )


        )
    }

    //////

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()) { }
        },
        bottomBar = {
            NextButton(
                onClick = { onClick() },
                enabled = state.code.all { it != null },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },

        content = { paddingValues ->
            OtpScreen1Content(
                paddingValues = paddingValues,
                state = state,
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
                }

            )

        }

    )

}
