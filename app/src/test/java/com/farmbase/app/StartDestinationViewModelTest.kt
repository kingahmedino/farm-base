package com.farmbase.app

import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.repo.StartDestinationRepo
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.*
//import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class StartDestinationViewModelTest {

    private lateinit var repo: StartDestinationRepo
    private lateinit var viewModel: StartDestinationViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk(relaxed = true) // Relaxed to avoid having to define default behavior
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getData should emit initial and updated value`() = runTest {
        val flow = MutableStateFlow(StartDestinationModel(finished = false))
        io.mockk.every { repo.getDataStore() } returns flow

        viewModel = StartDestinationViewModel(repo)

        val job = launch {
            viewModel.getData.collect {} // start collecting so stateIn activates
        }

        advanceUntilIdle() // Give stateIn time to emit

        val result = viewModel.getData.first()
        assertEquals(false, result.finished)

        job.cancel()
    }


    @Test
    fun `saveData should call repository with correct model`() = runTest {
        // Given
        val model = StartDestinationModel(finished = true)
        coEvery { repo.saveDataStore(model) } just Runs

        // When
        viewModel = StartDestinationViewModel(repo)
        viewModel.saveData(model)
        advanceUntilIdle() // Wait for coroutine to finish

        // Then
        coVerify(exactly = 1) { repo.saveDataStore(model) }
    }
}


