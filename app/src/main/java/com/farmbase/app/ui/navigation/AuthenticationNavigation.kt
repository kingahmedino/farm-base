package com.farmbase.app.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.farmbase.app.auth.AuthModel
import com.farmbase.app.auth.ui.components.otp.otpscreen1.OtpScreen1
import com.farmbase.app.auth.ui.components.otp.otpscreen2.OtpScreen2
import com.farmbase.app.auth.ui.login.LoginScreen
import com.farmbase.app.auth.ui.screens.SplashScreen
import com.farmbase.app.ui.navigation.target.NavigationAuth
import kotlin.reflect.typeOf

fun NavGraphBuilder.authenticationRoute(
    modifier: Modifier,
    navHostController: NavHostController
) {

    composable<NavigationAuth.OtpScreen1> { backStackEntry ->
        val args = backStackEntry.toRoute<NavigationAuth.OtpScreen1>()

        OtpScreen1(
            navController = navHostController,
            modifier = modifier,
            args = args
        )
    }

    composable<NavigationAuth.OtpScreen2>(
        typeMap = mapOf(
            typeOf<AuthModel>() to NavigationConstants.CustomNavType<AuthModel>(
                AuthModel::class,
                AuthModel.serializer()
            )
        )
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<NavigationAuth.OtpScreen2>()

        OtpScreen2(
            navController = navHostController,
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