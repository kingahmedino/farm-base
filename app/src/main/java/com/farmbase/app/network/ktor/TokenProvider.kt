package com.farmbase.app.network.ktor


/**
 * auth/TokenProvider.kt
 * Interface for token management
 */

/**
 * Interface for providing and refreshing authentication tokens
 */
interface TokenProvider {
    /**
     * Get the current authentication token
     */
    fun getToken(): String?

    /**
     * Refresh the authentication token
     * @return true if refresh was successful, false otherwise
     */
    suspend fun refreshToken(): Boolean
}


/**
 * SimpleTokenProvider.kt
 * A simple implementation of TokenProvider
 */

/**
 * Simple implementation of TokenProvider that stores tokens in memory
 */
class SimpleTokenProvider(
    private var token: String? = null,
    private val refreshCallback: (suspend () -> String?)? = null
) : TokenProvider {

    override fun getToken(): String? {
        return token
    }

    override suspend fun refreshToken(): Boolean {
        val newToken = refreshCallback?.invoke()
        if (newToken != null) {
            token = newToken
            return true
        }
        return false
    }

    /**
     * Update the token
     */
    fun updateToken(newToken: String?) {
        token = newToken
    }
}