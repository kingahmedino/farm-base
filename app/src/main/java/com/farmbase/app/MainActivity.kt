package com.farmbase.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.ui.navigation.Screen
import com.farmbase.app.ui.navigation.farmerNavGraph
import com.farmbase.app.ui.theme.FarmBaseTheme
import com.farmbase.app.ui.theme.PLCodingGray
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FarmBaseTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
//                    startDestination = Screen.Auth.route
                    startDestination = Screen.OtpScreen1.route
                ) {
                    farmerNavGraph(navController)
                }

            }
        }
    }
}
