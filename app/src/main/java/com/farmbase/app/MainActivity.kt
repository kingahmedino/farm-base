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

class MainActivity : ComponentActivity() {
    private val database by lazy { FarmerDatabase.getDatabase(applicationContext) }
    private val repository by lazy { FarmerRepository(database.farmerDao()) }
    private val farmerListViewModel by viewModels<FarmerListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FarmerListViewModel(repository) as T
            }
        }
    }
    private val farmerRegistrationViewModel by viewModels<FarmerRegistrationViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FarmerRegistrationViewModel(repository) as T
            }
        }
    }

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
                            viewModel = farmerListViewModel,
                            onAddNewFarmer = { navController.navigate("farmerRegistration") },
                            onEditFarmer = { farmerId -> navController.navigate("farmerEdit/$farmerId") }
                        )
                    }
                    composable("farmerRegistration") {
                        FarmerRegistrationScreen(
                            viewModel = farmerRegistrationViewModel,
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
                            viewModel = farmerRegistrationViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            farmerId = farmerId
                        )
                    }
                }
            }
        }
    }
}
