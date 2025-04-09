package com.farmbase.app.ui.navigation

import FarmerRegistrationScreen
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.otpscreen1.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.otpscreen2.OtpScreen2
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

    // deep link version
    data object OtpScreen1 :
        Screen("otpScreen1/{status}?accessToken={accessToken}&refreshToken={refreshToken}&resetPin={resetPin}")


    data object OtpScreen2 :
        Screen("otpScreen2?otpCode={otpCode}&accessToken={accessToken}&refreshToken={refreshToken}&resetPin={resetPin}") {
        fun createRoute(
            otpCode: String,
            accessToken: String,
            refreshToken: String,
            resetPin: Boolean
        ): String {
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

    data object MyHomepage : Screen("myHomepage?role={role}") {
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

    data object Detail :
        Screen("detail/{status}?accessToken={accessToken}&refreshToken={refreshToken}")

}

fun NavGraphBuilder.farmerNavGraph(navController: NavController, innerPadding: PaddingValues) {

    composable(
        route = Screen.OtpScreen1.route,

        arguments = listOf(
            navArgument("status") { type = NavType.StringType; defaultValue = "" },
            navArgument("accessToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("refreshToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("resetPin") { type = NavType.BoolType; defaultValue = false }
        )

    ) { navBackStackEntry ->
        OtpScreen1(
            navController = navController,
            navBackStackEntry = navBackStackEntry
        )
    }


    composable(
        route = Screen.OtpScreen2.route,
        arguments = listOf(
            navArgument("otpCode") { type = NavType.StringType },
            navArgument("accessToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("refreshToken") { type = NavType.StringType; defaultValue = "" },
            navArgument("resetPin") { type = NavType.BoolType; defaultValue = false },
        )
    ) { navBackStackEntry ->
        OtpScreen2(
            navController = navController,
            navBackStackEntry = navBackStackEntry,
            onBackButtonClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.Auth.route) {
        SplashScreen()
    }

    composable(Screen.Login.route) {

        LoginScreen(
            navController = navController,

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
            onNextButtonClicked = { navController.navigate(Screen.SelectHomepage.route) }
        )
    }

    composable(Screen.SelectHomepage.route) {
        SelectHomepageScreen(
            onBackButtonClicked = { navController.navigateUp() },
            onNextButtonClicked = { navController.navigate(Screen.MyHomepage.createRoute("Poultry Hub Lead")) }
        )
    }

    composable(
        Screen.MyHomepage.route,
        arguments = listOf(
            navArgument("role") {
                type = NavType.StringType
                nullable = false
                defaultValue = ""
            }
        ),
    ) { entry ->
        val role = entry.arguments?.getString("role")
        HomepageScreen(
            onBackButtonClicked = { navController.navigateUp() },
            role = role ?: ""

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

}

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED