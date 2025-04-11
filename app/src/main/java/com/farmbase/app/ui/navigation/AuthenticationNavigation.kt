package com.farmbase.app.ui.navigation

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.farmbase.app.auth.ui.components.otp.otpscreen1.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.otpscreen2.OtpScreen2
import com.farmbase.app.auth.ui.login.LoginScreen
import com.farmbase.app.auth.ui.screens.SplashScreen
import com.farmbase.app.ui.navigation.target.NavigationAuth

//import com.farmbase.app.ui.navigation.target.Screens

fun NavGraphBuilder.authenticationRoute(
    modifier: Modifier,
    navHostController: NavHostController
) {

    composable<NavigationAuth.OtpScreen1> { backStackEntry ->
        val args = backStackEntry.toRoute<NavigationAuth.OtpScreen1>()

        Log.d("authenticationRoute1", "authenticationRoute1: ${args.toString()}")

        OtpScreen1(
            navController = navHostController,
            navBackStackEntry = backStackEntry,
            modifier = modifier,
            args = args
        )
    }

    composable<NavigationAuth.OtpScreen2> { backStackEntry ->
        val args = backStackEntry.toRoute<NavigationAuth.OtpScreen2>()
        Log.d("authenticationRoute2", "authenticationRoute2: ${args.toString()}")


        OtpScreen2(
            navController = navHostController,
            navBackStackEntry = backStackEntry,
            args = args,
            onBackButtonClicked = { navHostController.popBackStack() }
        )
    }

    composable<NavigationAuth.Auth> {
        SplashScreen()
    }

    composable<NavigationAuth.Login> {
        LoginScreen(
            navController = navHostController,
            modifier = modifier
        )
    }

}