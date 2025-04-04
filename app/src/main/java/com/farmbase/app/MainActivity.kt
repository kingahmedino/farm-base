package com.farmbase.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.globalsnackbar.ObserveAsEvents
import com.farmbase.app.auth.globalsnackbar.SnackBarViewModel
import com.farmbase.app.auth.globalsnackbar.SnackbarController
import com.farmbase.app.auth.internetconnectionobserver.ConnectivityViewModel
import com.farmbase.app.auth.sessionManager.SessionManager
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.ui.navigation.Screen
import com.farmbase.app.ui.navigation.farmerNavGraph
import com.farmbase.app.ui.theme.FarmBaseTheme
import com.farmbase.app.ui.theme.PLCodingGray
import com.farmbase.app.utils.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            FarmBaseTheme {
//
//                val navController = rememberNavController()
//
//                //  otpScreen1
//                LaunchedEffect(intent) {
//                    intent?.let { intent ->
//                        val uri = intent.data
//                        if (uri != null) { // Ensure it's a deep link and not a normal launch
//                            val status = uri.pathSegments.getOrNull(0) ?: ""
//
//                            // Extract extras instead of query parameters
//                            val accessToken = intent.getStringExtra("accessToken") ?: ""
//                            val refreshToken = intent.getStringExtra("refreshToken") ?: ""
//
//                            navController.navigate("otpScreen1/$status?accessToken=$accessToken&refreshToken=$refreshToken") {
//                                popUpTo("home") { inclusive = true } // Keep "home" as the root
//                            }
//                        }
//                    }
//                }
//
//                NavHost(
//                    navController = navController,
//                 //   startDestination = Screen.Auth.route
//                    startDestination = Screen.OtpScreen1.route
//                ) {
//                    farmerNavGraph(navController)
//                }
//
//            }
//        }
//    }
//}

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

                val context = LocalContext.current

                val viewmodel: StartDestinationViewModel = hiltViewModel()
                val getData by viewmodel.getData.collectAsStateWithLifecycle()

                // global snack bar

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                val scope = rememberCoroutineScope()

                // Collect session timeout event and show Snackbar
                LaunchedEffect(Unit) {
                    sessionManager.sessionTimeoutFlow.collect {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "User inactive for 30 seconds",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }

                ObserveAsEvents(
                    flow = SnackbarController.events,
                    snackbarHostState
                ) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()

                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Long
                        )

                        if(result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }

                ObserveAsEvents(flow = SnackbarController.dismissEvents, snackbarHostState) {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss() // Dismiss from ViewModel
                    }
                }

                // global snack bar

                // CheckInternetConnectivity
                CheckInternetConnectivity()


                val navController = rememberNavController()
                val showExitDialog = remember { mutableStateOf(false) }

                // Handle deep link navigation
//                LaunchedEffect(intent) {
//                    intent?.data?.let { uri ->
//                        val status = uri.pathSegments.getOrNull(0) ?: ""
//                        val accessToken = intent.getStringExtra("accessToken") ?: ""
//                        val refreshToken = intent.getStringExtra("refreshToken") ?: ""
//
//                        navController.navigate("otpScreen1/$status?accessToken=$accessToken&refreshToken=$refreshToken") {
//                            popUpTo("home") { inclusive = true }
//                        }
//                    }
//                }

                LaunchedEffect(intent) {
                    intent?.data?.let { uri ->

                        val status = uri.pathSegments.getOrNull(0) ?: ""
                        val accessToken = intent.getStringExtra("accessToken") ?: ""
                        val refreshToken = intent.getStringExtra("refreshToken") ?: ""
                        // val resetPin = intent.getBooleanExtra("resetPin", false)
                        val resetPin = intent.getStringExtra("resetPin")?.toBoolean() ?: false


                        Log.d("TAG", "status: $status")
                        Log.d("TAG", "accessToken: $accessToken")
                        Log.d("TAG", "refreshToken: $refreshToken")
                        Log.d("TAG", "resetPin: $resetPin")

                        navController.navigate("otpScreen1/$status?accessToken=$accessToken&refreshToken=$refreshToken&resetPin=$resetPin") {
                            // Remove the popUpTo call, or replace it with a valid route
                            // popUpTo("home") { inclusive = true }
                            launchSingleTop = true;
                        }

                    }
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
                            hostState = snackbarHostState
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),

                        startDestination = getStartDestination(getData.finished)

                     //   startDestination = Screen.Auth.route
//                     startDestination = Screen.OtpScreen1.route
                    ) {
                        farmerNavGraph(navController, innerPadding)
                    }

                }


            }
        }
    }

    @Composable
    private fun CheckInternetConnectivity(
        connectivityViewModel : ConnectivityViewModel = hiltViewModel(),
        snackBarViewModel : SnackBarViewModel = hiltViewModel()
        ){

        val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
        val context = LocalContext.current

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        var initialConnectionState by remember { mutableStateOf(true) } // Track initial state

        LaunchedEffect(isConnected) {
            if (!isConnected && !initialConnectionState) { // Only show if not connected AND not initial
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "No Internet Connection",
                        duration = SnackbarDuration.Short
                    )
                }

                snackBarViewModel.showSnackbar()
//                Toast.makeText(context, "I am $isConnected", Toast.LENGTH_SHORT).show()
            }

            initialConnectionState = false // Update initial state after first composition
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) } // Attach SnackbarHost
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Connected? $isConnected"
                )
            }
        }
    }

    fun getStartDestination(checkStartDestination: Boolean) : String{

//        // true as per completed on boarding
//        if (checkStartDestination) return Scrabeen.Login.route
//
//        // false as per not completed on boarding
//        else return Screen.Auth.route

//      return Screen.Auth.route
        return Screen.Login.route
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


