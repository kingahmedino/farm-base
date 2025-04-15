package com.farmbase.app.auth.util

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.farmbase.app.MainActivity

// this would be used for tracking uxcam screen names
@Composable
fun TrackCurrentScreen(navController: NavHostController){
    val context = LocalContext.current

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val destination = backStackEntry.destination.route

            // Simple Log
            Log.d("Navigation", "Current destination: $destination")

            // Optional: Show Toast
                Toast.makeText(context, "Navigated to: $destination", Toast.LENGTH_SHORT).show()

        }
    }

}