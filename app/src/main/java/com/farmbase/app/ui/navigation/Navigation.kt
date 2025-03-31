package com.farmbase.app.ui.navigation

import FarmerRegistrationScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.OtpScreen2
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.auth.ui.screens.SplashScreen
import com.farmbase.app.models.Farmer
import com.farmbase.app.ui.farmerlist.FarmerListScreen
import com.farmbase.app.ui.formBuilder.FormBuilder
import com.farmbase.app.utils.HashHelper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

sealed class Screen(val route: String) {

    data object Auth : Screen("auth")

   // data object OtpScreen1 : Screen("otpScreen1")

    // deep link version
    data object OtpScreen1 : Screen("otpScreen1/{status}?accessToken={accessToken}&refreshToken={refreshToken}")


    data object OtpScreen2 : Screen("otpScreen2?otpCode={otpCode}") {
        fun createRoute(otpCode: String): String {
            val encodedOtp = URLEncoder.encode(otpCode, UTF_8.toString())
            return "otpScreen2?otpCode=$encodedOtp"
        }
    }


    data object FarmerList : Screen("farmerList")
    data object FarmerRegistration : Screen("farmerRegistration?farmerJson={farmerJson}") {
        fun createRoute(farmer: Farmer? = null): String {
            return if (farmer != null) {
                val jsonString = Json.encodeToString(farmer)
                val encodedJson = URLEncoder.encode(jsonString, UTF_8.toString())
                "farmerRegistration?farmerJson=$encodedJson"
            } else {
                "farmerRegistration?farmerJson="
            }
        }
    }

    data object NewForm : Screen("formBuilder")

    data object Detail : Screen("detail/{status}?accessToken={accessToken}&refreshToken={refreshToken}")

}

fun NavGraphBuilder.farmerNavGraph(navController: NavController) {

    composable(
      //  Screen.OtpScreen1.route
        route = Screen.OtpScreen1.route,

        arguments = listOf(
            navArgument("status") { type = NavType.StringType; defaultValue = "" },
            navArgument("accessToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("refreshToken") { type = NavType.StringType; defaultValue = "" }
        )

    ) { navBackStackEntry ->

        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->

            // otp
            val viewModel: OtpViewModel = hiltViewModel(navBackStackEntry) // Retain ViewModel

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

            val status = navBackStackEntry.arguments?.getString("status") ?: "N/A"
            val accessToken = navBackStackEntry.arguments?.getString("accessToken") ?: "N/A"
            val refreshToken = navBackStackEntry.arguments?.getString("refreshToken") ?: "N/A"

            // box and column

//            Box(
//                modifier = Modifier.fillMaxSize().padding(innerPadding),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    /** hide $status $accessToken $refreshToken**/
//                    Text(text = "Status: $status")
//                    Text(text = "Access Token: $accessToken")
//                    Text(text = "Refresh Token: $refreshToken")




            OtpScreen1(
                onClick = {
//                    viewModel.firstOtpCodeData = state.code.toString()
//                    navController.navigate(Screen.OtpScreen2.route)

                    val otpCode =
                        state.code.joinToString("") // Convert the list of digits to a string

                    val hashed4DigitCode = HashHelper.sha256(otpCode)

                   // viewModel.firstOtpCodeData = otpCode
                    navController.navigate(Screen.OtpScreen2.createRoute(hashed4DigitCode))

                },

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
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )

            // otp

      // box and column
        //  }}

        }
    }

    composable(Screen.OtpScreen2.route, arguments = listOf(
        navArgument("otpCode") { type = NavType.StringType }
    )) { navBackStackEntry ->

        val otpCode = navBackStackEntry.arguments?.getString("otpCode") ?: ""

        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->

            // otp
            val viewModel: OtpViewModel = hiltViewModel(navBackStackEntry) // Retain ViewModel


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
                if(allNumbersEntered) {
                    focusRequesters.forEach {
                        it.freeFocus()
                    }
                    focusManager.clearFocus()
                    keyboardManager?.hide()
                }
            }

            OtpScreen2(
                onClick = {},
                otpCode = otpCode,

                state = state,
                focusRequesters = focusRequesters,
                onAction = { action ->
                    when(action) {
                        is OtpAction.OnEnterNumber -> {
                            if(action.number != null) {
                                focusRequesters[action.index].freeFocus()
                            }
                        }
                        else -> Unit
                    }
                    viewModel.onAction(action)
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
                viewModel = viewModel
            )
            // otp

        }
    }

    composable(Screen.Auth.route) {
        SplashScreen()
    }

    composable(Screen.FarmerList.route) {
        FarmerListScreen(
            onAddNewFarmer = {
                navController.navigate(Screen.NewForm.route)
            },
            onEditFarmer = { farmer ->
                navController.navigate(Screen.FarmerRegistration.createRoute(farmer))
            }
        )
    }

    composable(
        route = Screen.FarmerRegistration.route,
        arguments = listOf(
            navArgument("farmerJson") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val farmerJson = backStackEntry.arguments?.getString("farmerJson")
        val farmer = farmerJson?.let { encodedJson ->
            try {
                val decodedJson = URLDecoder.decode(encodedJson, UTF_8.toString())
                Json.decodeFromString<Farmer>(decodedJson)
            } catch (e: Exception) {
                null
            }
        }

        FarmerRegistrationScreen(
            onNavigateBack = { navController.popBackStack() },
            farmer = farmer
        )
    }

    composable(Screen.NewForm.route) {
        FormBuilder()
    }



    composable(
//        route = "detail/{status}?accessToken={accessToken}&refreshToken={refreshToken}",
        route = Screen.Detail.route,

        arguments = listOf(
            navArgument("status") { type = NavType.StringType; defaultValue = "" },
            navArgument("accessToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("refreshToken") { type = NavType.StringType; defaultValue = "" }
        )
    ) { entry ->
        val status = entry.arguments?.getString("status") ?: "N/A"
        val accessToken = entry.arguments?.getString("accessToken") ?: "N/A"
        val refreshToken = entry.arguments?.getString("refreshToken") ?: "N/A"

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Status: $status")
                Text(text = "Access Token: $accessToken")
                Text(text = "Refresh Token: $refreshToken")
            }
        }
    }

}