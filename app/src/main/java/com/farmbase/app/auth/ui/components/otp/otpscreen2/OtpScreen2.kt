package com.farmbase.app.auth.ui.components.otp.otpscreen2

//import com.farmbase.app.ui.navigation.target.Screens
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
import androidx.navigation.NavController
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.ui.navigation.target.NavigationAuth
import com.farmbase.app.ui.navigation.target.NavigationHomepage
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.utils.Constants
import com.farmbase.app.utils.HashHelper
import com.farmbase.app.utils.SharedPreferencesManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun OtpScreen2(
    navController: NavController,
    onBackButtonClicked: () -> Unit,
//    viewModel: OtpViewModel = hiltViewModel(),
//    startDestinationViewModel: StartDestinationViewModel = hiltViewModel(),
    viewModel: OtpViewModel = koinViewModel(),
    startDestinationViewModel: StartDestinationViewModel = koinViewModel(),
    args: NavigationAuth.OtpScreen2?
) {
    val context = LocalContext.current

    val otpCode = args?.authModel?.otpCode ?: ""
    val accessToken = args?.authModel?.accessToken ?: ""
    val refreshToken = args?.authModel?.refreshToken ?: ""
    val resetPin = args?.authModel?.resetPin ?: false

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
            navController.navigate(NavigationHomepage.SelectProgram)
        } else {
            navController.navigate(NavigationHomepage.ConfirmAction)
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





