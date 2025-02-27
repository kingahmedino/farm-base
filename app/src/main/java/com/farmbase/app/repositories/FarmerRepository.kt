package com.farmbase.app.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.farmbase.app.database.FarmerDao
import com.farmbase.app.models.Farmer
import com.farmbase.app.sync.SyncWorker
import kotlinx.coroutines.flow.Flow
import java.time.Duration

class FarmerRepository(private val farmerDao: FarmerDao) {
    private val pagingConfig = PagingConfig(
        pageSize = 50,
        prefetchDistance = 150,
        enablePlaceholders = true,
        maxSize = 400
    )

    val allFarmers: Flow<List<Farmer>> = farmerDao.getAllFarmers()

    suspend fun insertFarmer(farmer: Farmer) {
        farmerDao.insertFarmer(farmer)
        scheduleSync()
    }

    suspend fun updateFarmer(farmer: Farmer) {
        farmerDao.updateFarmer(farmer)
    }

    suspend fun getFarmerById(id: Int): Farmer? {
        return farmerDao.getFarmerById(id)
    }

    // Get paged farmers
    fun getPagedFarmers(): Flow<PagingData<Farmer>> {
        return Pager(pagingConfig) {
            farmerDao.getPaginatedFarmers()
        }.flow
    }

    // Insert farmers with background sync
    suspend fun insertFarmers(farmers: List<Farmer>) {
        farmerDao.insertFarmers(farmers)
        scheduleSync()
    }

    // Sync worker for background operations
    private fun scheduleSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(false)
                    .build()
            )
            .build()

        WorkManager.getInstance()
            .beginUniqueWork(
                "sync_database",
                ExistingWorkPolicy.REPLACE,
                syncRequest
            ).enqueue()
    }
}