package com.farmbase.app.ui.navigation

import FarmerRegistrationScreen
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.OtpScreen2
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.auth.ui.login.LoginScreen
import com.farmbase.app.auth.ui.screens.SplashScreen
import com.farmbase.app.models.Farmer
import com.farmbase.app.ui.confirmAction.ConfirmActionScreen
import com.farmbase.app.ui.farmerlist.FarmerListScreen
import com.farmbase.app.ui.formBuilder.FormBuilder
import com.farmbase.app.ui.homepage.HomepageScreen
import com.farmbase.app.ui.selectHomepage.SelectHomepageScreen
import com.farmbase.app.ui.selectProgram.SelectProgramScreen
import com.farmbase.app.utils.Constants
import com.farmbase.app.utils.HashHelper
import com.farmbase.app.utils.SharedPreferencesManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

sealed class Screen(val route: String) {

    data object Auth : Screen("auth")

   // data object OtpScreen1 : Screen("otpScreen1")

    // deep link version
    data object OtpScreen1 : Screen("otpScreen1/{status}?accessToken={accessToken}&refreshToken={refreshToken}&resetPin={resetPin}")


//    data object OtpScreen2 : Screen("otpScreen2?otpCode={otpCode}") {
//        fun createRoute(otpCode: String): String {
//            val encodedOtp = URLEncoder.encode(otpCode, UTF_8.toString())
//            return "otpScreen2?otpCode=$encodedOtp"
//        }
//    }

    data object OtpScreen2 : Screen("otpScreen2?otpCode={otpCode}&accessToken={accessToken}&refreshToken={refreshToken}&resetPin={resetPin}") {
        fun createRoute(otpCode: String, accessToken: String, refreshToken: String, resetPin: Boolean): String {
            val encodedOtp = URLEncoder.encode(otpCode, UTF_8.toString())
            val encodedAccessToken = URLEncoder.encode(accessToken, UTF_8.toString())
            val encodedRefreshToken = URLEncoder.encode(refreshToken, UTF_8.toString())
            return "otpScreen2?otpCode=$encodedOtp&accessToken=$encodedAccessToken&refreshToken=$encodedRefreshToken&resetPin=$resetPin"
        }
    }


    data object Login : Screen("login")

    data object ConfirmAction : Screen("confirmActionScreen")

    data object SelectProgram : Screen("selectProgram")

    data object SelectHomepage : Screen("selectHomepage")

    data object MyHomepage : Screen("myHomepage?role={role}"){
        fun createRoute(role: String): String {
            return "myHomepage?role=$role"
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

fun NavGraphBuilder.farmerNavGraph(navController: NavController, innerPadding: PaddingValues) {

    composable(
      //  Screen.OtpScreen1.route
        route = Screen.OtpScreen1.route,

        arguments = listOf(
            navArgument("status") { type = NavType.StringType; defaultValue = "" },
            navArgument("accessToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("refreshToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("resetPin") { type = NavType.BoolType; defaultValue = false }
        )

    ) { navBackStackEntry ->

//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//        ) { innerPadding ->

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
            val resetPin = navBackStackEntry.arguments?.getBoolean("resetPin") ?: false

            // box and column

//            Box(
//                modifier = Modifier.fillMaxSize().padding(innerPadding),
//                contentAlignment = Alignment.Center
//            ) {

//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//                    /** hide $status $accessToken $refreshToken**/
//                    Text(text = "Status: $status")
//                    Text(text = "Access Token: $accessToken")
//                    Text(text = "Refresh Token: $refreshToken")


                    OtpScreen1(
                        innerPadding = innerPadding,
                        onClick = {
//                    viewModel.firstOtpCodeData = state.code.toString()
//                    navController.navigate(Screen.OtpScreen2.route)

                            val otpCode =
                                state.code.joinToString("") // Convert the list of digits to a string

//                            val hashed4DigitCode = HashHelper.sha256(otpCode)

                            // viewModel.firstOtpCodeData = otpCode
//                            navController.navigate(Screen.OtpScreen2.createRoute(hashed4DigitCode))
                            val hashed4DigitCode = HashHelper.sha256(otpCode)
                            navController.navigate(Screen.OtpScreen2.createRoute(hashed4DigitCode, accessToken, refreshToken, resetPin))


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
//                            .padding(innerPadding)
//                            .consumeWindowInsets(innerPadding)
                    )

                    // otp
               // }
      // box and column
        //  }}

      // scaffold  }
    }

//    composable(Screen.OtpScreen2.route, arguments = listOf(
//        navArgument("otpCode") { type = NavType.StringType }
//    )) { navBackStackEntry ->

    composable(
        route = Screen.OtpScreen2.route,
        arguments = listOf(
            navArgument("otpCode") { type = NavType.StringType },
            navArgument("accessToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("refreshToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("resetPin") { type = NavType.BoolType; defaultValue = false },
        )
    ) { navBackStackEntry ->

        val otpCode = navBackStackEntry.arguments?.getString("otpCode") ?: ""
        val accessToken = navBackStackEntry.arguments?.getString("accessToken") ?: ""
        val refreshToken = navBackStackEntry.arguments?.getString("refreshToken") ?: ""
        val resetPin = navBackStackEntry.arguments?.getBoolean("resetPin") ?: false

        val context = LocalContext.current

//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//        ) { innerPadding ->

            // otp
            val viewModel: OtpViewModel = hiltViewModel(navBackStackEntry) // Retain ViewModel

            val startDestinationViewModel: StartDestinationViewModel = hiltViewModel() // start destination ViewModel


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
                onClick = {

                    // set start destination
                    val setStartDestinationModel = StartDestinationModel(finished = true)

                    startDestinationViewModel.saveData(setStartDestinationModel)
                    // set start destination

                    // save access and refresh token in encrypted shared prefs

                    SharedPreferencesManager(context).encryptedPut(
                        key = "user4digitToken",
                        value = otpCode
                    )

                    SharedPreferencesManager(context).encryptedPut(
                        key = "accessToken",
                        value = accessToken
                    )

                    SharedPreferencesManager(context).encryptedPut(
                        key = "refreshToken",
                        value = refreshToken
                    )

                    val programId = SharedPreferencesManager(context).encryptedGet(key = Constants.SELECTED_PROGRAM_ID)
                    Log.d("Program Id", programId.toString())

                    // save access and refresh token in encrypted shared prefs

                    // navigate
                    if (programId.isNullOrBlank()) {
                        navController.navigate(Screen.SelectProgram.route)
                    } else {
                        navController.navigate(Screen.ConfirmAction.route)
                    }

//                    Toast.makeText(context, "$accessToken |||| $refreshToken", Toast.LENGTH_LONG).show()
                },

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
                modifier = Modifier,
//                    .padding(innerPadding)
//                    .consumeWindowInsets(innerPadding),
                viewModel = viewModel,
                onBackButtonClicked = { navController.popBackStack() }
            )
            // otp

       // }
    }

    composable(Screen.Auth.route) {
        SplashScreen(innerPadding = innerPadding)
    }

    composable(Screen.Login.route) {
//        SplashScreen(innerPadding = innerPadding)

        // otp
        val viewModel: OtpViewModel = hiltViewModel() // Retain ViewModel

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





        LoginScreen(
            innerPadding = innerPadding,
            onClick = {
//                    viewModel.firstOtpCodeData = state.code.toString()
//                    navController.navigate(Screen.OtpScreen2.route)

                val otpCode =
                    state.code.joinToString("") // Convert the list of digits to a string

//                            val hashed4DigitCode = HashHelper.sha256(otpCode)

                // viewModel.firstOtpCodeData = otpCode
//                            navController.navigate(Screen.OtpScreen2.createRoute(hashed4DigitCode))
                val hashed4DigitCode = HashHelper.sha256(otpCode)

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



    }

    composable(Screen.ConfirmAction.route) {
        ConfirmActionScreen(
            onBackButtonClicked = { navController.navigateUp() },
            onContinueClicked = { navController.navigate(Screen.SelectHomepage.route) },
            onSelectAnotherClicked = { navController.navigate(Screen.SelectProgram.route) }
        )
    }

    composable(Screen.SelectProgram.route) {
        SelectProgramScreen(
            onNextButtonClicked = {navController.navigate(Screen.SelectHomepage.route)}
        )
    }

    composable(Screen.SelectHomepage.route) {
        SelectHomepageScreen(
            onBackButtonClicked = { navController.navigateUp() },
            onNextButtonClicked = {navController.navigate(Screen.MyHomepage.createRoute("Poultry Hub Lead"))}
        )
    }

    composable(Screen.MyHomepage.route,
        arguments = listOf(
            navArgument("role") {
                type = NavType.StringType
                nullable = false
                defaultValue = ""
            }
        )
    ) { entry ->
        val role = entry.arguments?.getString("role")
        HomepageScreen(
            onBackButtonClicked = { navController.navigateUp() },
            role = role?:""

        )
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