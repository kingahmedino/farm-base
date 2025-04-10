package com.farmbase.app.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farmbase.app.auth.ui.components.otp.otpscreen1.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.otpscreen2.OtpScreen2
import com.farmbase.app.auth.ui.login.LoginScreen
import com.farmbase.app.auth.ui.screens.SplashScreen

fun NavGraphBuilder.authenticationRoute(
    modifier: Modifier,
    navHostController: NavHostController
) {

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
            navController = navHostController,
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
            navController = navHostController,
            navBackStackEntry = navBackStackEntry,
            onBackButtonClicked = { navHostController.popBackStack() }
        )
    }

    composable<Screens.Auth> {
        SplashScreen()
    }

    composable<Screens.Login> {
        LoginScreen(
            navController = navHostController,
            modifier = modifier
        )
    }

}