package com.farmbase.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.farmbase.app.ui.homepage.HomepageDetailScreen
import com.farmbase.app.ui.selectProgram.SelectProgramScreen

@Composable
fun HomepageNavigation(navHostController: NavHostController, startDestination: String) {
    // navHost defines the navigation host with a given startDestination
    NavHost(navController = navHostController, startDestination = startDestination ){
        // TODO:  add login screens
        // navigation(startDestination = "", route = "Login") {}

        navigation(startDestination = NavigationDestinations.SelectProgram.route, route = "Homepage") {
            composable(route = NavigationDestinations.SelectProgram.route) {
                SelectProgramScreen(
                    onNextButtonClicked = {
                        navHostController.navigateToSingleTop(NavigationDestinations.Homepage("Poultry Hub Lead"))
                    }
                )
            }
            composable<NavigationDestinations.Homepage> {
                val argument = it.toRoute<NavigationDestinations.Homepage>()
                HomepageDetailScreen(
                    role = argument.role,
                    onBackButtonClicked = { navHostController.navigateUp()}
                )
            }
        }
    }
}

/**
 * Extension function for `NavHostController` to navigate to a route with specific behavior.
 * Ensures the navigation is handled in a way that avoids duplicate destinations and restores the state.
 *
 * @param route The destination route to navigate to.
 */
fun NavHostController.navigateToSingleTop(route: NavigationDestinations) {
    return this.navigate(route) {
        // Ensure we navigate to the start destination of the graph and avoid adding it multiple times
        popUpTo(graph.findStartDestination().id) {
            saveState = true // Save the current state to restore it later
        }
        launchSingleTop = true // Avoid creating multiple instances of the same destination
        restoreState = true // Restore previously saved state when navigating back
    }
}

