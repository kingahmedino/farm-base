package com.farmbase.app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.farmbase.app.utils.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.argThat
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocationUtilsTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockFusedLocationClient: FusedLocationProviderClient

    @Mock
    private lateinit var mockLocationManager: LocationManager

    @Mock
    private lateinit var mockLocationTask: Task<Location>

    private var locationUtils: LocationUtils? = null
    private lateinit var closeable: AutoCloseable

    @Before
    fun setup() {
        // Initialize Mockito mocks with closeable for mockito-android
        closeable = MockitoAnnotations.openMocks(this)

        // Configure mock context
        `when`(mockContext.packageManager).thenReturn(mock(PackageManager::class.java))
        `when`(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(
            mockLocationManager
        )

        // Set up location task mock
        `when`(mockFusedLocationClient.lastLocation).thenReturn(mockLocationTask)

        `when`(mockLocationTask.addOnSuccessListener(any<OnSuccessListener<Location>>())).thenReturn(
            mockLocationTask
        )
        `when`(
            mockLocationTask.addOnSuccessListener(
                any<Executor>(),
                any<OnSuccessListener<Location>>()
            )
        ).thenReturn(mockLocationTask)
        `when`(mockLocationTask.addOnFailureListener(any<OnFailureListener>())).thenReturn(
            mockLocationTask
        )

        `when`(
            mockFusedLocationClient.requestLocationUpdates(
                any<LocationRequest>(),
                any<LocationCallback>(),
                any<Looper>()
            )
        ).thenReturn(mock(Task::class.java) as Task<Void>)

        // Create LocationUtils with mocked dependencies
        locationUtils = LocationUtils(
            context = mockContext,
            fusedLocationClient = mockFusedLocationClient,
            locationManager = mockLocationManager
        )
    }

    @After
    fun tearDown() {
        // Safely call cleanup if locationUtils is not null
        locationUtils?.cleanup()
        locationUtils = null

        // Close mocks to prevent memory leaks
        closeable.close()
    }

    @Test
    fun testHasLocationPermissionsFineLocationGranted() {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Simulate fine location permission
        mockLocationPermission(
            fineLocationPermission = PackageManager.PERMISSION_GRANTED,
            coarseLocationPermission = PackageManager.PERMISSION_DENIED
        )

        assertTrue(utils.hasLocationPermissions())
    }

    @Test
    fun testHasLocationPermissionsCoarseLocationGranted() {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Simulate coarse location permission
        mockLocationPermission(
            fineLocationPermission = PackageManager.PERMISSION_DENIED,
            coarseLocationPermission = PackageManager.PERMISSION_GRANTED
        )

        assertTrue(utils.hasLocationPermissions())
    }

    @Test
    fun testHasLocationPermissionsNoPermissions() {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // No permissions granted
        mockLocationPermission(
            fineLocationPermission = PackageManager.PERMISSION_DENIED,
            coarseLocationPermission = PackageManager.PERMISSION_DENIED
        )

        assertFalse(utils.hasLocationPermissions())
    }

    @Test
    fun testLocationEnabledFlowUpdatesOnLocationServicesChange() = runTest {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Setup: initial location services state
        mockLocationProviders(gpsEnabled = false, networkEnabled = false)

        // Initial state should be false
        assertFalse(utils.locationEnabledFlow.first())

        // Simulate location services being enabled
        mockLocationProviders(gpsEnabled = true, networkEnabled = false)

        // Create a mock broadcast intent
        val intent = Intent(LocationManager.PROVIDERS_CHANGED_ACTION)
        utils.locationStateReceiver.onReceive(mockContext, intent)

        // Check if the flow updates
        assertTrue(utils.locationEnabledFlow.first())
    }

    @Test
    fun testGetCurrentLocationReturnsNullWhenNoPermissions() = runTest {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Simulate no location permissions
        mockLocationPermission(
            fineLocationPermission = PackageManager.PERMISSION_DENIED,
            coarseLocationPermission = PackageManager.PERMISSION_DENIED
        )

        // Attempt to get current location
        val location = utils.getCurrentLocation()

        // Assert null is returned
        assertNull(location)
    }

    @Test
    fun testGetCurrentLocationReturnsNullWhenLocationDisabled() = runTest {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Simulate location permissions granted but location service disabled
        mockLocationPermission(
            fineLocationPermission = PackageManager.PERMISSION_GRANTED,
            coarseLocationPermission = PackageManager.PERMISSION_GRANTED
        )
        mockLocationProviders(gpsEnabled = false, networkEnabled = false)

        // Attempt to get current location with explicit locationEnabled = false
        val location = utils.getCurrentLocation(locationEnabled = false)

        // Assert null is returned
        assertNull(location)
    }

    @Test
    fun testGetLocationUpdatesFlowNoPermissions() = runTest {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Simulate no location permissions
        mockLocationPermission(
            fineLocationPermission = PackageManager.PERMISSION_DENIED,
            coarseLocationPermission = PackageManager.PERMISSION_DENIED
        )

        // Collect from the flow
        val locations = utils.getLocationUpdates().take(1).toList()

        // Verify no locations were emitted and no location updates were requested
        assertTrue(locations.isEmpty())
        verify(mockFusedLocationClient, never()).requestLocationUpdates(
            any<LocationRequest>(),
            any<LocationCallback>(),
            any<Looper>()
        )
    }

    @Test
    fun testCleanupUnregistersReceiver() {
        val utils = locationUtils ?: throw IllegalStateException("LocationUtils not initialized")

        // Call cleanup
        utils.cleanup()

        // Verify unregisterReceiver was called
        verify(mockContext).unregisterReceiver(any<BroadcastReceiver>())
    }


    // Helper methods to reduce code duplication and improve readability
    private fun mockLocationPermission(
        fineLocationPermission: Int,
        coarseLocationPermission: Int
    ) {
        `when`(
            ContextCompat.checkSelfPermission(
                mockContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ).thenReturn(fineLocationPermission)

        `when`(
            ContextCompat.checkSelfPermission(
                mockContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ).thenReturn(coarseLocationPermission)
    }

    private fun mockLocationProviders(gpsEnabled: Boolean, networkEnabled: Boolean) {
        `when`(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(
            gpsEnabled
        )
        `when`(mockLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(
            networkEnabled
        )
    }
}
