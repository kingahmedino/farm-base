package com.farmbase.app.authTests

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.repo.StartDestinationRepo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StartDestinationRepoTest {

    private lateinit var context: Context
    private lateinit var repo: StartDestinationRepo
    private lateinit var testDatastore: DataStore<Preferences>

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        testDatastore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("test_datastore")
        }
        repo = StartDestinationRepo(context)
    }

    @Test
    fun saveAndReadDataStore_shouldMatchSavedValue() = runTest {
        val testModel = StartDestinationModel(finished = true)

        repo.saveDataStore(testModel)

        val result = repo.getDataStore().first()
        assertEquals(testModel, result)
    }
}
