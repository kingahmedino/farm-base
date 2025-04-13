package com.farmbase.app.auth.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.auth.globalsnackbar.SnackBarViewModel
import com.farmbase.app.auth.internetconnectionobserver.ConnectivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckInternetConnectivity(
//    connectivityViewModel: ConnectivityViewModel = hiltViewModel(),
//    snackBarViewModel: SnackBarViewModel = hiltViewModel(),

    connectivityViewModel: ConnectivityViewModel = koinViewModel(),
    snackBarViewModel: SnackBarViewModel = koinViewModel(),

    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
    var initialConnectionState by remember { mutableStateOf(true) } // Track initial state

    LaunchedEffect(isConnected) {
        if (!isConnected && !initialConnectionState) { // Only show if not connected AND not initial
            coroutineScope.launch {

                snackBarHostState.currentSnackbarData?.dismiss()

                snackBarViewModel.showSnackbar()

            }
        }
        initialConnectionState = false // Update initial state after first composition
    }

    @Composable
    fun checkInternetAvailability(){

        LaunchedEffect(isConnected) {
            if (!isConnected && !initialConnectionState) { // Only show if not connected AND not initial
                coroutineScope.launch {

                    snackBarHostState.currentSnackbarData?.dismiss()

                    snackBarViewModel.showSnackbar()

                }
            }
            initialConnectionState = false // Update initial state after first composition
        }

    }
}