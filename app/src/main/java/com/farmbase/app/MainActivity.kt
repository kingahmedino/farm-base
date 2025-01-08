package com.farmbase.app

import FarmerRegistrationScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.farmbase.app.database.FarmerDatabase
import com.farmbase.app.repositories.FarmerRepository
import com.farmbase.app.ui.farmerlist.FarmerListScreen
import com.farmbase.app.ui.farmerlist.FarmerListViewModel
import com.farmbase.app.ui.farmerregistration.FarmerRegistrationViewModel
import com.farmbase.app.ui.theme.FarmBaseTheme
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
                NavHost(navController, startDestination = "farmerList") {
                    composable("farmerList") {
                        FarmerListScreen(
                            onAddNewFarmer = { navController.navigate("farmerRegistration") },
                            onEditFarmer = { farmerId -> navController.navigate("farmerEdit/$farmerId") }
                        )
                    }
                    composable("farmerRegistration") {
                        FarmerRegistrationScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        "farmerEdit/{farmerId}",
                        arguments = listOf(navArgument("farmerId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val farmerId =
                            backStackEntry.arguments?.getInt("farmerId") ?: return@composable
                        FarmerRegistrationScreen(
                            onNavigateBack = { navController.popBackStack() },
                            farmerId = farmerId
                        )
                    }
                }
            }
        }
    }
}
