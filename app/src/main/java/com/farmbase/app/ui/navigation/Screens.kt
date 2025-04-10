package com.farmbase.app.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screens {

    @Serializable
    data object Login : Screens()

    @Serializable
    data object ConfirmAction : Screens()

    @Serializable
    data object SelectProgram : Screens()

    @Serializable
    data object SelectHomepage : Screens()

    @Serializable
    data object Auth : Screens()

    @Serializable
    data object NewForm : Screens()

    @Serializable
    data object FarmerList : Screens()

}
