package com.farmbase.app.ui.navigation.target

import kotlinx.serialization.Serializable

sealed interface NavigationHomepage : NavigationTarget {

    @Serializable
    data object ConfirmAction : NavigationHomepage

    @Serializable
    data object SelectProgram : NavigationHomepage

    @Serializable
    data object SelectHomepage : NavigationHomepage

    @Serializable
    data object NewForm : NavigationHomepage

    @Serializable
    data object FarmerList : NavigationHomepage

    @Serializable
    data class MyHomepage(val role: String) : NavigationHomepage

}