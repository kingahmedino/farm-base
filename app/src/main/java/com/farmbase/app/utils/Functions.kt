package com.farmbase.app.utils

import android.util.Base64
import android.util.Log
import com.google.gson.Gson

object Functions {

    fun decodeTokenPayload(token: String): TokenPayLoad {
        val parts = token.split(".")
        if (parts.size != 3) throw IllegalArgumentException("Invalid JWT token")

        val payload = parts[1]
        val data =  String(Base64.decode(payload, Base64.URL_SAFE))
        Log.d("access", data)
        return Gson().fromJson(data, TokenPayLoad::class.java)
    }
}