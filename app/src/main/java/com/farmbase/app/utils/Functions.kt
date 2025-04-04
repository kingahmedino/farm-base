package com.farmbase.app.utils

import android.util.Base64
import android.util.Log

object Functions {

    fun decodeTokenPayload(token: String): String {
        val parts = token.split(".")
        if (parts.size != 3) throw IllegalArgumentException("Invalid JWT token")

        val payload = parts[1]
        Log.d("Token", String(Base64.decode(payload, Base64.URL_SAFE)))
        return String(Base64.decode(payload, Base64.URL_SAFE))
    }
}