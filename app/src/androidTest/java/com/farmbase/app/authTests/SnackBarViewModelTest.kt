package com.farmbase.app.authTests

import com.farmbase.app.auth.globalsnackbar.SnackBarController
import com.farmbase.app.auth.globalsnackbar.SnackBarViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SnackBarViewModelTest {

    private lateinit var viewModel: SnackBarViewModel

    @Before
    fun setup() {
        viewModel = SnackBarViewModel()
    }

    @Test
    fun showSnackbar_sendsSnackBarEvent() = runTest {
        val job = launch {
            val event = SnackBarController.events.first()
            assertEquals("Internet Connection Lost", event.message)
            assertEquals("Okay!", event.action?.name)
        }

        viewModel.showSnackbar()
        job.cancel()
    }

    @Test
    fun dismissSnackbar_sendsDismissEvent() = runTest {
        val job = launch {
            val dismissed = SnackBarController.dismissEvents.first()
            assertEquals(Unit, dismissed)
        }

        viewModel.dismissSnackbar()
        job.cancel()
    }
}
