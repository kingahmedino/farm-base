package com.farmbase.app.network.ktor.error

/**
 * error/ErrorResponse.kt
 * Data class for parsing error responses from the API
 */

import kotlinx.serialization.Serializable

/**
 * Standard error response format
 */
@Serializable
data class ErrorResponse(
    val message: String? = null,
    val errors: Map<String, List<String>> = emptyMap()
)