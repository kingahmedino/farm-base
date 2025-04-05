package com.farmbase.app.network.ktor

/**
 * KtorClient.kt
 * Core network client implementation with dynamic URL support
 */
import com.farmbase.app.network.ktor.error.ApiError
import com.farmbase.app.network.ktor.error.ErrorHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

/**
 * Main network client for the application that supports dynamic base URLs
 */

object KtorClient {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
        encodeDefaults = true
    }

    val errorHandler = ErrorHandler(json)

    var tokenProvider: TokenProvider? = null

    internal var isDebugMode = false

    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    // Default timeouts
    private var connectionTimeoutSeconds = 30L
    private var requestTimeoutSeconds = 60L

    // Main HTTP client
    private val client by lazy {
        createHttpClient()
    }

    /**
     * Configure the network client
     */
    fun configure(
        tokenProvider: TokenProvider? = null,
        debug: Boolean = false,
        connectionTimeout: Long = 30,
        requestTimeout: Long = 60,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        this.tokenProvider = tokenProvider
        this.isDebugMode = debug
        this.connectionTimeoutSeconds = connectionTimeout
        this.requestTimeoutSeconds = requestTimeout
        this.dispatcher = dispatcher
    }

    /**
     * Create HTTP client with current configuration
     */
    private fun createHttpClient(): HttpClient {
        return HttpClient(Android) {
            // Install ContentNegotiation to handle JSON serialization/deserialization
            install(ContentNegotiation) {
                gson {
                    setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    setPrettyPrinting()
                }
            }

            // Configure logging based on debug mode
            if (isDebugMode) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("KtorClient: $message")
                        }
                    }
                }
            }

            // Configure timeouts
            install(HttpTimeout) {
                requestTimeoutMillis = requestTimeoutSeconds.seconds.inWholeMilliseconds
                socketTimeoutMillis = requestTimeoutSeconds.seconds.inWholeMilliseconds
                connectTimeoutMillis = connectionTimeoutSeconds.seconds.inWholeMilliseconds
            }

            // Configure response observer for token refresh
            install(ResponseObserver) {
                onResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        // Don't block here, let the call method handle the refresh if needed
                        if (isDebugMode) {
                            println("KtorClient: Received 401 Unauthorized")
                        }
                    }
                }
            }

            // Configure default content type
            defaultRequest {
                contentType(ContentType.Application.Json)
            }

            // Error handling configuration
            expectSuccess = false // Don't throw exceptions for non-2xx responses
        }
    }

    /**
     * Generic function to make API calls with proper error handling
     * @param url The complete URL or path to append to the base URL
     * @param method The HTTP method for the request
     * @param body Optional request body
     * @param queryParams Optional query parameters
     * @param headers Optional request headers
     * @param requiresAuth Whether the request requires authentication
     * @param progressListener Optional upload progress listener
     * @return Result containing either the response or an error
     */
    suspend inline fun <reified T> call(
        url: String,
        method: HttpMethod = HttpMethod.Get,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        requiresAuth: Boolean = true,
        noinline progressListener: ((bytesSentTotal: Long, contentLength: Long) -> Unit)? = null
    ): Result<T> = callInternal(
        url = url,
        method = method,
        body = body,
        queryParams = queryParams,
        headers = headers,
        requiresAuth = requiresAuth,
        progressListener = progressListener,
        type = T::class
    )

    /**
     * Non-inline implementation of call that is used by the inline wrapper
     * This solves the issue with inline functions accessing private members
     */
    @PublishedApi
    internal suspend inline fun <reified T : Any> callInternal(
        url: String,
        method: HttpMethod,
        body: Any?,
        queryParams: Map<String, String>,
        headers: Map<String, String>,
        requiresAuth: Boolean,
        noinline progressListener: ((bytesSentTotal: Long, contentLength: Long) -> Unit)?,
        type: KClass<*>
    ): Result<T> = withContext(dispatcher) {
        try {
            val response = performRequest(
                url = url,
                method = method,
                body = body,
                queryParams = queryParams,
                headers = headers,
                requiresAuth = requiresAuth,
                progressListener = progressListener
            )

            // Handle unauthorized error with token refresh if available
            if (response.status == HttpStatusCode.Unauthorized && requiresAuth) {
                // Try to refresh the token
                val tokenRefreshed = tokenProvider?.refreshToken() ?: false
                if (tokenRefreshed) {
                    // Retry the request with the new token
                    return@withContext performRequest(
                        url = url,
                        method = method,
                        body = body,
                        queryParams = queryParams,
                        headers = headers,
                        requiresAuth = requiresAuth,
                        progressListener = progressListener
                    ).let { retryResponse ->
                        processResponse(retryResponse, type)
                    }
                }
            }

            // Process the response
            return@withContext processResponse(response, type)

        } catch (e: Exception) {
            when (e) {
                is java.net.UnknownHostException, is java.io.IOException -> {
                    // Network-related errors
                    Result.failure(errorHandler.handleNetworkError(e))
                }
                is SerializationException -> {
                    // Serialization/deserialization errors
                    Result.failure(errorHandler.handleSerializationError(e))
                }
                else -> {
                    // Generic errors
                    Result.failure(errorHandler.handleGenericError(e))
                }
            }
        }
    }

    /**
     * Perform the actual HTTP request
     */
    suspend fun performRequest(
        url: String,
        method: HttpMethod,
        body: Any?,
        queryParams: Map<String, String>,
        headers: Map<String, String>,
        requiresAuth: Boolean,
        progressListener: ((bytesSentTotal: Long, contentLength: Long) -> Unit)?
    ): HttpResponse {
        return client.request {
            this.method = method

            // Handle URL - determine if it's a full URL or just a path
            url {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    takeFrom(url)
                } else {
                    // Treat as a path to be appended
                    encodedPath = url
                }
            }

            // Add request body if provided
            if (body != null) {
                setBody(body)
            }

            // Add query parameters
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }

            // Add custom headers
            headers.forEach { (key, value) ->
                header(key, value)
            }

            // Add authorization header if required
            if (requiresAuth) {
                tokenProvider?.getToken()?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }

            // Add progress listener if provided
            progressListener?.let { listener ->
                onUpload { bytesSentTotal, contentLength ->
                    listener(bytesSentTotal, contentLength ?: 0)
                }
            }
        }
    }

    /**
     * Process the HTTP response and convert to Result
     */
    suspend inline fun <reified T : Any> processResponse(response: HttpResponse, type: KClass<*>): Result<T> {
        return when {
            response.status.isSuccess() -> {
                // Handle special case for Unit return type
                if (type == Unit::class) {
                    Result.success(Unit as T)
                } else {
                    // Parse the response body
                    try {
                        Result.success(response.body<T>())
                    } catch (e: SerializationException) {
                        Result.failure(errorHandler.handleSerializationError(e))
                    }
                }
            }
            else -> {
                // Handle error response
                Result.failure(errorHandler.handleHttpError(response))
            }
        }
    }

    /**
     * Convenience method for GET requests
     */
    suspend inline fun <reified T> get(
        url: String,
        queryParams: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        requiresAuth: Boolean = true
    ): Result<T> = call(
        url = url,
        method = HttpMethod.Get,
        queryParams = queryParams,
        headers = headers,
        requiresAuth = requiresAuth
    )

    /**
     * Convenience method for POST requests
     */
    suspend inline fun <reified T> post(
        url: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        requiresAuth: Boolean = true,
        noinline progressListener: ((bytesSentTotal: Long, contentLength: Long) -> Unit)? = null
    ): Result<T> = call(
        url = url,
        method = HttpMethod.Post,
        body = body,
        queryParams = queryParams,
        headers = headers,
        requiresAuth = requiresAuth,
        progressListener = progressListener
    )

    /**
     * Convenience method for PUT requests
     */
    suspend inline fun <reified T> put(
        url: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        requiresAuth: Boolean = true
    ): Result<T> = call(
        url = url,
        method = HttpMethod.Put,
        body = body,
        queryParams = queryParams,
        headers = headers,
        requiresAuth = requiresAuth
    )

    /**
     * Convenience method for DELETE requests
     */
    suspend inline fun <reified T> delete(
        url: String,
        queryParams: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        requiresAuth: Boolean = true
    ): Result<T> = call(
        url = url,
        method = HttpMethod.Delete,
        queryParams = queryParams,
        headers = headers,
        requiresAuth = requiresAuth
    )

    /**
     * Convenience method for PATCH requests
     */
    suspend inline fun <reified T> patch(
        url: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        requiresAuth: Boolean = true
    ): Result<T> = call(
        url = url,
        method = HttpMethod.Patch,
        body = body,
        queryParams = queryParams,
        headers = headers,
        requiresAuth = requiresAuth
    )
}
