package com.farmbase.app.utils

import com.google.gson.annotations.SerializedName

data class TokenPayLoad (
    val sub: String,
    val iat: Int,
    val sessionId: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val tenantId: String,
    val roleIds: List<String>,
    val roles: List<String>,
    @SerializedName("token_creation_timestamp")
    val tokenCreationTimestamp: Long,
    @SerializedName("token_expiry_timestamp")
    val tokenExpiryTimestamp : Long,
    val exp: Int,
    val jti: String
)