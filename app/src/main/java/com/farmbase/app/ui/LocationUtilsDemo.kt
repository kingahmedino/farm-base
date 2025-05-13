package com.farmbase.app.ui

import android.location.Location
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmbase.app.utils.LocationUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationUtilsDemo() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationUtils = remember { LocationUtils(context) }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // location
    var singleLocation by remember { mutableStateOf<Location?>(null) }
    var continuousLocation by remember { mutableStateOf<Location?>(null) }

    var lastUpdated by remember { mutableStateOf<Date?>(null) }
    var isTracking by remember { mutableStateOf(false) }
    var trackingJob by remember { mutableStateOf<Job?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var locationTimeout by remember { mutableLongStateOf(20000L) } // 20 seconds default
    var timeoutToastShown by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val hapticFeedback = LocalHapticFeedback.current

    // Collect location services status from Flow
    var locationServicesEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        locationUtils.locationEnabledFlow.collect { isEnabled ->
            locationServicesEnabled = isEnabled
            // If location services are disabled while tracking, show error
            if (!isEnabled && isTracking) {
                errorMessage = "Location services disabled. Tracking paused."
            } else if (isEnabled && errorMessage == "Location services disabled. Tracking paused.") {
                errorMessage = null
            }
        }
    }

    // Function to trigger timeout feedback
    val handleLocationTimeout: () -> Unit = {
        // Ensure toast is shown on the main thread
        Handler(Looper.getMainLooper()).post {
            // Provide multiple feedback mechanisms
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

            // Always show toast, reset the flag each time
            Toast.makeText(
                context,
                "Location request timed out after ${locationTimeout / 1000} seconds",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Location Checker Demo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Status card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Permissions:")
                    Text(
                        text = if (locationPermissions.allPermissionsGranted) "Granted ✓" else "Missing ✗",
                        color = if (locationPermissions.allPermissionsGranted)
                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Location Services:")
                    Text(
                        text = if (locationServicesEnabled) "Enabled ✓" else "Disabled ✗",
                        color = if (locationServicesEnabled)
                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }

                // Error message if any
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Single location section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Single Location Update",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                // Timeout slider
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Timeout: ${locationTimeout / 1000} seconds",
                        fontSize = 14.sp
                    )
                    Slider(
                        value = locationTimeout.toFloat(),
                        onValueChange = { locationTimeout = it.toLong() },
                        valueRange = 5000f..20000f,
                        steps = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                singleLocation?.let {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Lat: ${String.format("%.6f", it.latitude)}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Lng: ${String.format("%.6f", it.longitude)}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Accuracy: ${String.format("%.1f", it.accuracy)} m",
                            fontSize = 14.sp
                        )
                    }
                } ?: Text(
                    text = if (isLoadingLocation) "Getting location..." else "No location data available",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )

                Button(
                    onClick = {
                        if (!locationPermissions.allPermissionsGranted) {
                            locationPermissions.launchMultiplePermissionRequest()
                        } else if (!locationServicesEnabled) {
                            errorMessage = "Please enable location services"
                        } else {
                            errorMessage = null
                            isLoadingLocation = true
                            scope.launch {
                                try {
                                    singleLocation = locationUtils.getCurrentLocation(
                                        locationEnabled = locationServicesEnabled,
                                        timeoutMs = locationTimeout,
                                        onTimeout = handleLocationTimeout
                                    )

                                    if (singleLocation == null) {
                                        errorMessage = "Could not get location. Please try again."
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                } finally {
                                    isLoadingLocation = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoadingLocation
                ) {
                    if (isLoadingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Get Current Location")
                    }
                }
            }
        }

        // Continuous tracking section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Continuous Location Tracking",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                continuousLocation?.let {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Lat: ${String.format("%.6f", it.latitude)}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Lng: ${String.format("%.6f", it.longitude)}",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Accuracy: ${String.format("%.1f", it.accuracy)} m",
                            fontSize = 14.sp
                        )
                        lastUpdated?.let { date ->
                            Text(
                                text = "Last update: ${dateFormatter.format(date)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } ?: Text(
                    text = if (isTracking) "Waiting for updates..." else "Not tracking",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (!locationPermissions.allPermissionsGranted) {
                                locationPermissions.launchMultiplePermissionRequest()
                                return@Button
                            }

                            if (!locationServicesEnabled) {
                                errorMessage = "Please enable location services"
                                return@Button
                            }

                            errorMessage = null
                            isTracking = true

                            trackingJob?.cancel()
                            trackingJob = locationUtils.getLocationUpdates(
                                updateIntervalMs = 3000 // Update every 3 seconds
                            )
                                .onEach { location ->
                                    continuousLocation = location
                                    lastUpdated = Date()
                                }
                                .catch { e ->
                                    errorMessage = "Error: ${e.message}"
                                    isTracking = false
                                }
                                .launchIn(scope)
                        },
                        enabled = !isTracking && locationServicesEnabled,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start Tracking")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            trackingJob?.cancel()
                            trackingJob = null
                            isTracking = false
                        },
                        enabled = isTracking,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Stop Tracking")
                    }
                }

                // Update interval indicator
                if (isTracking) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

    // Clean up resources when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            trackingJob?.cancel()
            locationUtils.cleanup() // Properly clean up the LocationUtils
        }
    }
}
