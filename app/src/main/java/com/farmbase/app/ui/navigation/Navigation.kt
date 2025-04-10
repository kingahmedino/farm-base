package com.farmbase.app.ui.navigation

import FarmerRegistrationScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farmbase.app.auth.ui.components.otp.otpscreen1.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.otpscreen2.OtpScreen2
import com.farmbase.app.auth.ui.login.LoginScreen
import com.farmbase.app.auth.ui.screens.SplashScreen
import com.farmbase.app.models.Farmer
import com.farmbase.app.ui.confirmAction.ConfirmActionScreen
import com.farmbase.app.ui.farmerlist.FarmerListScreen
import com.farmbase.app.ui.formBuilder.FormBuilder
import com.farmbase.app.ui.homepage.HomepageScreen
import com.farmbase.app.ui.selectHomepage.SelectHomepageScreen
import com.farmbase.app.ui.selectProgram.SelectProgramScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

sealed class Screen(val route: String) {

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


    data object MyHomepage : Screen("myHomepage?role={role}") {
        fun createRoute(role: String): String {
            return "myHomepage?role=$role"
        }
    }

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

    composable<Screens.ConfirmAction> {
        ConfirmActionScreen(
            onBackButtonClicked = { navController.navigateUp() },
            onContinueClicked = { navController.navigate(Screens.SelectHomepage) },
            onSelectAnotherClicked = { navController.navigate(Screens.SelectProgram) }
        )
    }

    composable<Screens.SelectProgram> {
        SelectProgramScreen(
            onNextButtonClicked = { navController.navigate(Screens.SelectHomepage) }
        )
    }

    composable<Screens.SelectHomepage> {
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

    composable<Screens.FarmerList> {
        FarmerListScreen(
            onAddNewFarmer = {
                navController.navigate(Screens.NewForm)
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

    composable<Screens.NewForm> {
        FormBuilder()
    }

}