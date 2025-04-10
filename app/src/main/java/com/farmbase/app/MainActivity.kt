package com.farmbase.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.sessionManager.SessionManager
import com.farmbase.app.auth.util.CheckInternetConnectivity
import com.farmbase.app.auth.util.CheckUserInactivity
import com.farmbase.app.ui.navigation.Screen
import com.farmbase.app.ui.navigation.Screens
import com.farmbase.app.ui.navigation.farmerNavGraph
import com.farmbase.app.ui.theme.FarmBaseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FarmBaseTheme {

                val viewmodel: StartDestinationViewModel = hiltViewModel()
                val getData by viewmodel.getData.collectAsStateWithLifecycle()

                // global snack bar
                val snackBarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                // global snack bar

                // CheckInternetConnectivity
                CheckInternetConnectivity(snackBarHostState = snackBarHostState, coroutineScope = coroutineScope)
                CheckUserInactivity(sessionManager = sessionManager, snackBarHostState = snackBarHostState, coroutineScope = coroutineScope)

                val navController = rememberNavController()
                val showExitDialog = remember { mutableStateOf(false) }

                // State to track if getStartDestination has been called
                var startDestination by remember { mutableStateOf<Screens?>(null) }

                // Call getStartDestination only once
                if (startDestination == null && getData.finished != null) {
                    startDestination = getStartDestination(getData.finished)
                }

                // Back press handler
                BackHandler {
                    showExitDialog.value = true
                }

                // Show exit confirmation dialog
                if (showExitDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showExitDialog.value = false },
                        title = { Text("Exit App") },
                        text = { Text("Are you sure you want to close the app?") },
                        confirmButton = {
                            TextButton(onClick = { finish() }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showExitDialog.value = false }) {
                                Text("No")
                            }
                        }
                    )
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarHostState
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (startDestination != null) {
                        NavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            startDestination = startDestination!!
                        ) {
                            farmerNavGraph(navController, innerPadding)
                        }

                        LaunchedEffect(intent) {
                            intent?.data?.let { uri ->
                                val status = uri.pathSegments.getOrNull(0) ?: ""
                                val accessToken = intent.getStringExtra("accessToken") ?: ""
                                val refreshToken = intent.getStringExtra("refreshToken") ?: ""
                                val resetPin = intent.getStringExtra("resetPin")?.toBoolean() ?: false

                                Log.d("TAG", "status: $status")
                                Log.d("TAG", "accessToken: $accessToken")
                                Log.d("TAG", "refreshToken: $refreshToken")
                                Log.d("TAG", "resetPin: $resetPin")

                                navController.navigate("otpScreen1/$status?accessToken=$accessToken&refreshToken=$refreshToken&resetPin=$resetPin") {
                                    launchSingleTop = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private fun getStartDestination(checkStartDestination: Boolean?): Screens {
        return if (checkStartDestination == null || !checkStartDestination) Screens.Auth
        else Screens.Login
    }

    // session manager
    override fun onUserInteraction() {
        super.onUserInteraction()
        sessionManager.onUserInteraction()
    }

    override fun onResume() {
        super.onResume()
        sessionManager.startSessionTimer()
    }

    override fun onPause() {
        super.onPause()
        sessionManager.stopSessionTimer()
    }
}


