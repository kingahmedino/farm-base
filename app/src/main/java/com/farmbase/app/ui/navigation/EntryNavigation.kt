package com.farmbase.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@Composable
fun EntryNavigation(
    navHostController: NavHostController,
    startDestination: SplashScreen,
    modifier: Modifier
) {

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        authenticationRoute(modifier = modifier, navHostController = navHostController)

        homePageNavigationRoute(modifier = modifier, navHostController = navHostController)

    }
}


/**
 * Extension function for `NavHostController` to navigate to a route with specific behavior.
 * Ensures the navigation is handled in a way that avoids duplicate destinations and restores the state.
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

/**
 * Extension function for [NavController] to navigate to a specific screen.
 * Ensures that the same destination is not launched multiple times and preserves state.
 * @param route The destination screen to navigate to.
 */
fun NavController.navigateToScreen(route: NavigationDestinations) {
    return this.navigate(route) { // Navigate to the specified route
        launchSingleTop =
            true // Prevents multiple copies of the same destination from being created

        restoreState = true // Restores the previously saved state if available

        popBackStack(
            route,
            inclusive = false, // keeps the destination in the stack instead of removing it
            saveState = true // saves the state of the previous destination before popping
        )
    }
}

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED