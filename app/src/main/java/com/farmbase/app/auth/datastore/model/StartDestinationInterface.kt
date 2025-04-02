package com.farmbase.app.auth.datastore.model

import kotlinx.coroutines.flow.Flow

interface StartDestinationInterface {

    suspend fun saveDataStore(startDestinationModel: StartDestinationModel)

    fun getDataStore(): Flow<StartDestinationModel>

}