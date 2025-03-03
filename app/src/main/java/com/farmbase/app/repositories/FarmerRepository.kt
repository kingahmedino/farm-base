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
import com.farmbase.app.database.CropDao
import com.farmbase.app.database.FarmerDao
import com.farmbase.app.database.HarvestDao
import com.farmbase.app.models.Crop
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.Harvest
import com.farmbase.app.sync.SyncWorker
import kotlinx.coroutines.flow.Flow
import java.time.Duration

class FarmerRepository(
    private val farmerDao: FarmerDao,
    private val cropDao: CropDao,
    private val harvestDao: HarvestDao
) {
    val allFarmers: Flow<List<Farmer>> = farmerDao.getAllFarmers()

    suspend fun insertFarmer(farmer: Farmer) {
        farmerDao.insertFarmer(farmer)
        scheduleSync()
    }

    suspend fun updateFarmer(farmer: Farmer) {
        farmerDao.updateFarmer(farmer)
    }

    // Insert farmers with background sync
    suspend fun insertFarmers(farmers: List<Farmer>) {
        farmerDao.insertFarmers(farmers)
    }

    suspend fun insertCrops(crops: List<Crop>) {
        cropDao.insertCrops(crops)
    }

    suspend fun insertHarvests(harvests: List<Harvest>) {
        harvestDao.insertHarvests(harvests)
    }

    // Sync worker for background operations
    fun scheduleSync() {
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