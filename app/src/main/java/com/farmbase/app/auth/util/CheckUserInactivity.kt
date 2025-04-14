package com.farmbase.app.auth.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.globalsnackbar.ObserveAsEvents
import com.farmbase.app.auth.globalsnackbar.SnackBarViewModel
import com.farmbase.app.auth.globalsnackbar.SnackBarController
import com.farmbase.app.auth.sessionManager.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CheckUserInactivity(
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    sessionManager: SessionManager
){
    val messageText = convertRawStringToString(R.string.user_inactive_for_30_seconds)
    val buttonText = convertRawStringToString(R.string.okay)

    // Collect session timeout event and show Snackbar
    LaunchedEffect(Unit) {
        sessionManager.sessionTimeoutFlow.collect {
            coroutineScope.launch {

                snackBarViewModel.showAppSnackBar(
                    message = messageText,
                    buttonMessage = buttonText
                )

            }
        }
    }

    ObserveAsEvents(
        flow = SnackBarController.events,
        snackBarHostState
    ) { event ->
        coroutineScope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()

            val result = snackBarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Long
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    ObserveAsEvents(flow = SnackBarController.dismissEvents, snackBarHostState) {
        coroutineScope.launch {
            snackBarHostState.currentSnackbarData?.dismiss() // Dismiss from ViewModel
        }
    }

}