package com.farmbase.app.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AuthModel(
    val status: String,
    val otpCode: String,
    val accessToken: String,
    val refreshToken: String,
    val resetPin: Boolean
) : Parcelable
