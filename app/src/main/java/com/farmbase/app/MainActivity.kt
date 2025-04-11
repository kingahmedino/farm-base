package com.farmbase.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.sessionManager.SessionManager
import com.farmbase.app.auth.util.AppExitDialog
import com.farmbase.app.auth.util.CheckInternetConnectivity
import com.farmbase.app.auth.util.CheckUserInactivity
import com.farmbase.app.ui.navigation.EntryNavigation
import com.farmbase.app.ui.navigation.target.NavigationAuth
import com.farmbase.app.ui.navigation.target.NavigationTarget
//import com.farmbase.app.ui.navigation.target.Screens
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
                val navController = rememberNavController()

                // State to track if getStartDestination has been called
                var startDestination by remember { mutableStateOf<NavigationTarget?>(null) }

                // global snack bar

                // CheckInternetConnectivity
                CheckInternetConnectivity(snackBarHostState = snackBarHostState, coroutineScope = coroutineScope)
                CheckUserInactivity(sessionManager = sessionManager, snackBarHostState = snackBarHostState, coroutineScope = coroutineScope)
                AppExitDialog(this)

                // Call getStartDestination only once
                if (startDestination == null && getData.finished != null) {
                    startDestination = getStartDestination(getData.finished)
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

                        EntryNavigation(
                            navHostController = navController,
                            startDestination = startDestination!!,
                            modifier = Modifier.padding(innerPadding)
                        )

                        LaunchedEffect(intent) {
                            intent?.data?.let { uri ->
                                val status = uri.pathSegments.getOrNull(0) ?: ""
                                val accessToken = intent.getStringExtra("accessToken") ?: ""
                                val refreshToken = intent.getStringExtra("refreshToken") ?: ""
                                val resetPin = intent.getStringExtra("resetPin")?.toBoolean() ?: false

                                navController.navigate(
                                    NavigationAuth.OtpScreen1(
                                        status = status,
                                        accessToken = accessToken,
                                        refreshToken = refreshToken,
                                        resetPin = resetPin
                                    )
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    private fun getStartDestination(checkStartDestination: Boolean?): NavigationTarget {
        return if (checkStartDestination == null || !checkStartDestination) NavigationAuth.Auth
        else NavigationAuth.Login
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


