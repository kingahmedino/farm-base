package com.farmbase.app

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
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
import de.comahe.i18n4k.config.I18n4kConfigDelegate
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.providers.MessagesProviderViaText
import x.y.MyMessages

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initI18n4k(this)

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

    companion object {
        private var inited = false

        var i18n4kConfig = mutableStateOf(I18n4kConfigImmutable())

        fun initI18n4k(context: Context) {
            if (inited) return

            i18n4k = I18n4kConfigDelegate { i18n4kConfig.value }

            inited = true
        }
    }
}
