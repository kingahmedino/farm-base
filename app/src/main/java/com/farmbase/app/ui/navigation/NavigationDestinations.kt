package com.farmbase.app.ui.navigation
import com.farmbase.app.utils.ActivityCardItem
import kotlinx.serialization.Serializable


// Routes
@Serializable object SplashScreen
@Serializable object ConfirmAction

// Route for nested graph
@Serializable object Homepage

// Routes inside nested graph
@Serializable object SelectProgram
@Serializable object SelectHomepage
@Serializable data class MyHomepage(val role: String)
@Serializable data class ExecuteActivity(val activityCardItem: String)

/**
 * Possible destinations that can be navigated to within the app
 * */

@Serializable
sealed interface NavigationDestinationsInfo {
    val route: String
}

@Serializable
sealed class NavigationDestinations: NavigationDestinationsInfo {

    /** select program screen pointer */
    @Serializable
    data object SelectProgram: NavigationDestinations() {
        override val route: String
            get() = "Select Program"
    }

    /** select homepage screen pointer */
    @Serializable
    data object SelectHomepage: NavigationDestinations() {
        override val route: String
            get() = "Select Homepage"
    }

    /** homepage screen pointer */
    @Serializable
    data class Homepage(
        val role: String = ""
    ): NavigationDestinations() {
        override val route: String
            get() = "Homepage"
    }

}