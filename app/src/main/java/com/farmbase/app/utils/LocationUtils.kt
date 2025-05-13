package com.farmbase.app.utils

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume


/**
 * Utility class for accessing device location using Google's Fused Location Provider.
 *
 * `LocationUtils` supports:
 * - **One-time high-accuracy location requests** with timeout and fallback
 * - **Continuous location updates** as a reactive Kotlin [Flow]
 * - **Real-time monitoring of location service state** via [locationEnabledFlow]
 * - **Permission checks** for both coarse and fine location
 *
 * ### One-time location usage:
 * ```kotlin
 * lifecycleScope.launch {
 *     val locationUtils = LocationUtils(context)
 *     locationUtils.locationEnabledFlow.collect { isEnabled ->
 *         // React to location service status
 *     }
 *
 *     try {
 *         val location = locationUtils.getCurrentLocation()
 *         location?.let {
 *             // Use latitude, longitude, etc.
 *         }
 *     } catch (e: LocationTimeoutException) {
 *         // Handle timeout scenario
 *     }
 * }
 * ```
 *
 * ### Continuous location updates:
 * ```kotlin
 * val locationUtils = LocationUtils(context)
 * locationUtils.getLocationUpdates(updateIntervalMs = 3000)
 *     .onEach { location ->
 *         // Process each location update
 *     }
 *     .launchIn(lifecycleScope)
 * ```
 *
 *  @param context Android context used for system services and broadcast registration
 *  @param fusedLocationClient Optional: Custom [FusedLocationProviderClient] for testability. Defaults to Google's implementation.
 *  @param locationManager Optional: Custom [LocationManager] for testability. Defaults to system service.
 *
 */
class LocationUtils(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context),
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
) {

    /**
     * Checks if the app has been granted either coarse or fine location permissions.
     *
     * @return `true` if [Manifest.permission.ACCESS_FINE_LOCATION] or
     *         [Manifest.permission.ACCESS_COARSE_LOCATION] is granted; `false` otherwise.
     */
    fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabledInternal(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    val locationStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                _locationEnabledFlow.value = isLocationEnabledInternal()
            }
        }
    }

    private val _locationEnabledFlow = MutableStateFlow(isLocationEnabledInternal())

    /**
     * Emits the current state of location services (enabled/disabled) and updates when it changes.
     */
    val locationEnabledFlow: StateFlow<Boolean> = _locationEnabledFlow.asStateFlow()

    init {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(locationStateReceiver, filter)
    }

    /**
     * Retrieves the current location as a one-time request.
     *
     * This method:
     * - Verifies location permissions and whether location services are enabled
     * - Requests a high-accuracy location with an optional fallback to the last known location
     * - Supports timeout and an optional callback if the timeout is reached
     *
     * @param locationEnabled Current location service state (from [locationEnabledFlow]);
     *                        defaults to the latest internal state.
     * @param timeoutMs Maximum time to wait before aborting the request (default is 5000ms).
     * @param onTimeout Optional callback triggered if location retrieval times out.
     * @param forceRefresh Whether to request a fresh location or try using last known location first.
     *
     * @return The most recent [Location], or `null` if:
     *         - Permissions are missing
     *         - Location services are disabled
     *         - A valid location couldn't be retrieved within the timeout
     */
    suspend fun getCurrentLocation(
        locationEnabled: Boolean = _locationEnabledFlow.value,
        timeoutMs: Long = 5000,
        onTimeout: (() -> Unit)? = null,
        forceRefresh: Boolean = true
    ): Location? {
        if (!hasLocationPermissions() || !locationEnabled) {
            return null
        }

        return withTimeoutOrNull(timeoutMs) {
            suspendCancellableCoroutine { continuation ->
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        fusedLocationClient.removeLocationUpdates(this)
                        continuation.resume(result.lastLocation)
                    }
                }

                val singleUpdateRequest =
                    LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        if (forceRefresh) 0 else timeoutMs
                    )
                        .setMaxUpdates(1)
                        .setDurationMillis(timeoutMs)
                        .setWaitForAccurateLocation(true)
                        .build()

                try {
                    fusedLocationClient.requestLocationUpdates(
                        singleUpdateRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    ).addOnFailureListener {
                        if (forceRefresh) {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (continuation.isActive) continuation.resume(location)
                            }.addOnFailureListener {
                                if (continuation.isActive) continuation.resume(null)
                            }
                        } else if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }

                    if (!forceRefresh) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null && continuation.isActive) {
                                continuation.resume(location)
                            }
                        }
                    }

                    continuation.invokeOnCancellation {
                        fusedLocationClient.removeLocationUpdates(locationCallback)
                    }
                } catch (e: SecurityException) {
                    continuation.resume(null)
                }
            }
        } ?: run {
            onTimeout?.invoke()
            null
        }
    }

    /**
     * Starts a [Flow] of continuous location updates.
     *
     * This method is suitable for real-time tracking and reacts to each location update.
     * Automatically stops when the flow collector is cancelled and cleans up listeners.
     *
     * @param updateIntervalMs Desired interval for location updates in milliseconds.
     * @param fastestIntervalMs Fastest rate the app can handle updates (used for throttling).
     * @param priority Priority level for accuracy (e.g., [Priority.PRIORITY_HIGH_ACCURACY]).
     *
     * @return A [Flow] emitting [Location] objects as updates are received.
     *
     * @throws SecurityException If location permissions are not granted.
     */
    fun getLocationUpdates(
        updateIntervalMs: Long = 3000,
        fastestIntervalMs: Long = 2000,
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY
    ): Flow<Location> = callbackFlow {
        if (!hasLocationPermissions() || !_locationEnabledFlow.value) {
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(priority, updateIntervalMs)
            .setMinUpdateIntervalMillis(fastestIntervalMs)
            .setWaitForAccurateLocation(true)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            ).addOnFailureListener {
                close(it)
            }

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        } catch (e: SecurityException) {
            close(e)
        }
    }

    /**
     * Unregisters the broadcast receiver to avoid memory leaks.
     *
     * Call this when [LocationUtils] is no longer in use, such as in `onDestroy`.
     */
    fun cleanup() {
        try {
            context.unregisterReceiver(locationStateReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}

