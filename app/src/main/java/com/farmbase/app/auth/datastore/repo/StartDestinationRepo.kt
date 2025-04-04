package com.farmbase.app.auth.datastore.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.farmbase.app.auth.datastore.model.StartDestinationInterface
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val DataStore_NAME = "Start_Destination_DataStore"
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStore_NAME)


class StartDestinationRepo(private val context: Context) : StartDestinationInterface {

    companion object {
        val FINISHED = booleanPreferencesKey("FINISHED")

    }

    override suspend fun saveDataStore(startDestinationModel: StartDestinationModel) {
        context.datastore.edit { DS2s ->
            DS2s[FINISHED] = startDestinationModel.finished ?: false

        }
    }

    override fun getDataStore(): Flow<StartDestinationModel> =
        context.datastore.data.map { ds2 ->
            StartDestinationModel(
                finished = ds2[FINISHED] ?: false
            )

        }

}

