package com.farmbase.app.auth.ui.components.otp.otpscreen2

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.ui.navigation.Screen
import com.farmbase.app.ui.navigation.Screens
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.utils.Constants
import com.farmbase.app.utils.HashHelper
import com.farmbase.app.utils.SharedPreferencesManager

/*@Composable
fun OtpScreen2(
    navController: NavController,
    onBackButtonClicked: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel(),
    startDestinationViewModel: StartDestinationViewModel = hiltViewModel(),
    navBackStackEntry: NavBackStackEntry
) {

    val context = LocalContext.current

    val otpCode = navBackStackEntry.arguments?.getString("otpCode") ?: ""
    val accessToken = navBackStackEntry.arguments?.getString("accessToken") ?: ""
    val refreshToken = navBackStackEntry.arguments?.getString("refreshToken") ?: ""
    val resetPin = navBackStackEntry.arguments?.getBoolean("resetPin") ?: false


    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember {
        List(4) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    val onClick: () -> Unit = {

        // set start destination
        val setStartDestinationModel = StartDestinationModel(finished = true)

        startDestinationViewModel.saveData(setStartDestinationModel)
        // set start destination

        val userOtp = state.code.joinToString("")

        val hashedUserOtp = HashHelper.sha256(userOtp)

        // save access and refresh token in encrypted shared prefs

        SharedPreferencesManager(context).encryptedPut(
            key = "userOtp",
            value = hashedUserOtp
        )

        SharedPreferencesManager(context).encryptedPut(
            key = "accessToken",
            value = accessToken
        )

        SharedPreferencesManager(context).encryptedPut(
            key = "refreshToken",
            value = refreshToken
        )

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

    // hashed otp code
    viewModel.firstOtpCodeData = otpCode

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()) { onBackButtonClicked() }
        },
        bottomBar = {
            NextButton(
                onClick = {
                    // error is here, need to link dialogOpened here to
                    // dialogOpened in OtpScreen2Content
                    dialogOpened = true
                          },
                enabled = state.code.all { it != null },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },

        content = { paddingValues ->

            OtpScreen2Content(
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
                },
                onClick = onClick,
            )
        }
    )
}*/

@Composable
fun OtpScreen2(
    navController: NavController,
    onBackButtonClicked: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel(),
    startDestinationViewModel: StartDestinationViewModel = hiltViewModel(),
    navBackStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current

    val otpCode = navBackStackEntry.arguments?.getString("otpCode") ?: ""
    val accessToken = navBackStackEntry.arguments?.getString("accessToken") ?: ""
    val refreshToken = navBackStackEntry.arguments?.getString("refreshToken") ?: ""
    val resetPin = navBackStackEntry.arguments?.getBoolean("resetPin") ?: false

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember { List(4) { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    var dialogOpened by remember { mutableStateOf(false) }
    var userPinCreationSuccess by remember { mutableStateOf(false) }

    val onClick: () -> Unit = {
        val setStartDestinationModel = StartDestinationModel(finished = true)
        startDestinationViewModel.saveData(setStartDestinationModel)

        val userOtp = state.code.joinToString("")
        val hashedUserOtp = HashHelper.sha256(userOtp)

        SharedPreferencesManager(context).encryptedPut("userOtp", hashedUserOtp)
        SharedPreferencesManager(context).encryptedPut("accessToken", accessToken)
        SharedPreferencesManager(context).encryptedPut("refreshToken", refreshToken)

        val programId = SharedPreferencesManager(context).encryptedGet(Constants.SELECTED_PROGRAM_ID)
        Log.d("Program Id", programId.toString())

        if (programId.isNullOrBlank()) {
            navController.navigate(Screens.SelectProgram)
        } else {
            navController.navigate(Screens.ConfirmAction)
        }
    }

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach { it.freeFocus() }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

    viewModel.firstOtpCodeData = otpCode

    Scaffold(
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()) { onBackButtonClicked() }
        },
        bottomBar = {
            NextButton(
                onClick = {
                    dialogOpened = true // now works!
                },
                enabled = state.code.all { it != null },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        content = { paddingValues ->
            OtpScreen2Content(
                paddingValues = paddingValues,
                state = state,
                focusRequesters = focusRequesters,
                onAction = viewModel::onAction,
                onClick = onClick,
                dialogOpened = dialogOpened,
                onDialogOpenedChange = { dialogOpened = it },
                userPinCreationSuccess = userPinCreationSuccess,
                onPinSuccessChange = { userPinCreationSuccess = it }
            )
        }
    )
}





