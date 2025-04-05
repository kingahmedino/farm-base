package com.farmbase.app.network.ktor.error

import io.ktor.client.statement.bodyAsText


/**
 * error/ErrorHandler.kt
 * Handles API errors and transforms them into app-specific errors
 */

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Handles API errors and transforms them into app-specific ApiError types
 */
class ErrorHandler(private val json: Json) {
    /**
     * Handle HTTP errors based on status code
     */
    suspend fun handleHttpError(response: HttpResponse): ApiError {
        return when (response.status) {
            HttpStatusCode.Unauthorized -> {
                // Handle authentication errors
                ApiError.Unauthorized("Authentication required")
            }
            HttpStatusCode.Forbidden -> {
                // Handle authorization errors
                ApiError.Forbidden("You don't have permission to access this resource")
            }
            HttpStatusCode.NotFound -> {
                // Handle not found errors
                ApiError.NotFound("The requested resource was not found")
            }
            in HttpStatusCode.BadRequest..HttpStatusCode.UnsupportedMediaType -> {
                // Handle client errors (400-415)
                try {
                    val errorBody = response.bodyAsText()
                    try {
                        val errorResponse = json.decodeFromString<ErrorResponse>(errorBody)
                        ApiError.Client(
                            statusCode = response.status.value,
                            message = errorResponse.message ?: "Client error",
                            errors = errorResponse.errors
                        )
                    } catch (e: Exception) {
                        ApiError.Client(
                            statusCode = response.status.value,
                            message = "Client error: ${errorBody.take(100)}"
                        )
                    }
                } catch (e: Exception) {
                    ApiError.Client(
                        statusCode = response.status.value,
                        message = "Client error"
                    )
                }
            }
            in HttpStatusCode.InternalServerError..HttpStatusCode.InsufficientStorage -> {
                // Handle server errors (500-599)
                ApiError.Server("Server error occurred. Please try again later.")
            }
            else -> {
                // Handle other errors
                ApiError.Unknown("An unexpected error occurred")
            }
        }
    }

    /**
     * Handle network-related errors
     */
    fun handleNetworkError(exception: Exception): ApiError {
        return when (exception) {
            is java.net.UnknownHostException -> {
                ApiError.Network("No internet connection")
            }
            is java.net.SocketTimeoutException -> {
                ApiError.Timeout("Connection timed out")
            }
            else -> {
                ApiError.Network("Network error: ${exception.message}")
            }
        }
    }

    /**
     * Handle serialization/deserialization errors
     */
    fun handleSerializationError(exception: SerializationException): ApiError {
        return ApiError.ParseError("Failed to parse the response: ${exception.message}")
    }

    /**
     * Handle generic errors
     */
    fun handleGenericError(exception: Exception): ApiError {
        return ApiError.Unknown("Unknown error: ${exception.message}")
    }
}