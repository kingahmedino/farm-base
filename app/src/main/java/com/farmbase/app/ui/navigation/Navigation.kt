package com.farmbase.app.ui.navigation

import FarmerRegistrationScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farmbase.app.models.Farmer
import com.farmbase.app.ui.farmerlist.FarmerListScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

sealed class Screen(val route: String) {
    object FarmerList : Screen("farmerList")
    object FarmerRegistration : Screen("farmerRegistration?farmerJson={farmerJson}") {
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

fun NavGraphBuilder.farmerNavGraph(navController: NavController) {
    composable(Screen.FarmerList.route) {
        FarmerListScreen(
            onAddNewFarmer = {
                navController.navigate(Screen.FarmerRegistration.createRoute())
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
}