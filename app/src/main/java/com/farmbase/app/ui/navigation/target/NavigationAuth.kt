package com.farmbase.app.ui.navigation.target

import kotlinx.serialization.Serializable

sealed interface NavigationAuth : NavigationTarget {

    @Serializable
    data class OtpScreen1(
        val status: String,
        val accessToken: String,
        val refreshToken: String,
        val resetPin: Boolean
    ) : NavigationAuth

    @Serializable
    data class OtpScreen2(
        val otpCode: String,
        val accessToken: String,
        val refreshToken: String,
        val resetPin: Boolean
    ) : NavigationAuth

    @Serializable
    data object Login : NavigationAuth

    @Serializable
    data object Auth : NavigationAuth

}