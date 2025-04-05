package com.farmbase.app.network.ktor.error

/**
 * error/ApiError.kt
 * Sealed class hierarchy representing different types of API errors
 */

/**
 * Sealed class hierarchy representing different types of API errors
 */
sealed class ApiError(override val message: String) : Exception(message) {
    data class Network(override val message: String) : ApiError(message)
    data class Timeout(override val message: String) : ApiError(message)
    data class Unauthorized(override val message: String) : ApiError(message)
    data class Forbidden(override val message: String) : ApiError(message)
    data class NotFound(override val message: String) : ApiError(message)
    data class Client(
        val statusCode: Int,
        override val message: String,
        val errors: Map<String, List<String>> = emptyMap()
    ) : ApiError(message)

    data class Server(override val message: String) : ApiError(message)
    data class ParseError(override val message: String) : ApiError(message)
    data class Unknown(override val message: String) : ApiError(message)
}