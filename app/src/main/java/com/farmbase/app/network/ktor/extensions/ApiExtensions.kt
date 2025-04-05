package com.farmbase.app.network.ktor.extensions

/**
 * extensions/ApiExtensions.kt
 * Extension functions for working with the KtorClient
 */

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Executes an API call and returns the result as a Flow
 */
inline fun <reified T> apiCall(
    crossinline block: suspend () -> Result<T>
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading)

    val result = block()

    result.fold(
        onSuccess = { data ->
            emit(ApiResult.Success(data))
        },
        onFailure = { error ->
            emit(ApiResult.Error(error))
        }
    )
}

/**
 * Sealed class representing different states of an API call
 */
sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>()
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: Throwable) : ApiResult<Nothing>()
}