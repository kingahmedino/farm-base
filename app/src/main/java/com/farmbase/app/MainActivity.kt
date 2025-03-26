package com.farmbase.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.farmbase.app.ui.navigation.HomepageNavigation
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
                /*val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.FarmerList.route
                ) {
                    farmerNavGraph(navController)
                }*/

                val navHostController = rememberNavController()
                HomepageNavigation(
                    navHostController = navHostController,
                    startDestination = "Homepage"
                )
            }
        }
    }
}
