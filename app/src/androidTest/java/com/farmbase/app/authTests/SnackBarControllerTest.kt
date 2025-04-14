package com.farmbase.app.authTests

import com.farmbase.app.auth.globalsnackbar.SnackBarController
import com.farmbase.app.auth.globalsnackbar.SnackBarEvent
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SnackBarControllerTest {

    @Test
    fun sendEventshouldemitSnackBarEvent() = runTest {
        // Given
        val testEvent = SnackBarEvent(message = "Test message")

        val job = launch {
            val emitted = SnackBarController.events.first()
            assertEquals(testEvent.message, emitted.message)
        }

        // When
        SnackBarController.sendEvent(testEvent)

        job.cancelAndJoin()
    }

    @Test
    fun dismissSnackbarshouldemitUnit() = runTest {
        val job = launch {
            val emitted = SnackBarController.dismissEvents.first()
            assertEquals(Unit, emitted)
        }

        // When
        SnackBarController.dismissSnackbar()

        job.cancelAndJoin()
    }
}
