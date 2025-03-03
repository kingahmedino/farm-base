package com.farmbase.app.sync

import android.util.Log
import com.farmbase.app.database.FarmBaseDatabase
import com.farmbase.app.models.Crop
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.Harvest
import com.farmbase.app.models.SyncMetadata
import com.farmbase.app.utils.HeavySyncTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class SyncOrchestrator(
    private val database: FarmBaseDatabase,
) {
    private val tableConfigs = listOf(
        // High priority tables (sync first)
        TableSyncConfig("farmers", batchSize = 100, priority = TableSyncConfig.SyncPriority.HIGH),
        TableSyncConfig("crops", batchSize = 100, priority = TableSyncConfig.SyncPriority.HIGH),
        TableSyncConfig("harvests", batchSize = 100, priority = TableSyncConfig.SyncPriority.LOW),
    )

    // Extension function to get unsynced records for any table
    private suspend inline fun <reified T> getUnsyncedRecords(
        tableName: String,
        batchSize: Int
    ): List<T> {
        return when (tableName) {
            "farmers" -> database.farmerDao().getUnsynced(limit = batchSize)
            "crops" -> database.cropDao().getUnsynced(limit = batchSize)
            "harvests" -> database.harvestDao().getUnsynced(limit = batchSize)
            else -> emptyList()
        } as List<T>
    }

    suspend fun startSync() {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        try {
            // Sort tables by priority and dependencies
            val prioritizedTables = tableConfigs.groupBy { it.priority }

            //  The priority of jobs will run sequentially, while priority job will
            //  run all its child jobs in parallel to each other
            val highPriorityJobs =
                (prioritizedTables[TableSyncConfig.SyncPriority.HIGH] ?: emptyList()).map {
                    coroutineScope.syncTable(it)
                }
            highPriorityJobs.awaitAll()

            val mediumPriorityJobs =
                (prioritizedTables[TableSyncConfig.SyncPriority.MEDIUM] ?: emptyList()).map {
                    coroutineScope.syncTable(it)
                }
            mediumPriorityJobs.awaitAll()

            val lowPriorityJobs =
                (prioritizedTables[TableSyncConfig.SyncPriority.LOW] ?: emptyList()).map {
                    coroutineScope.syncTable(it)
                }
            lowPriorityJobs.awaitAll()

        } catch (e: Exception) {
            Log.e("SyncOrchestrator", "Sync failed", e)
        }
    }

    private fun CoroutineScope.syncTable(config: TableSyncConfig): Deferred<Unit> {
        return async {
            try {
                database.syncMetadataDao().updateSyncMetadata(
                    SyncMetadata(
                        tableName = config.tableName,
                        syncStatus = SyncMetadata.SyncStatus.IN_PROGRESS,
                        lastSyncTime = System.currentTimeMillis(),
                        priority = config.priority
                    )
                )

                // Upload local changes
                uploadChanges(config)

                // Download changes from server
                downloadChanges(config)

                database.syncMetadataDao().updateSyncMetadata(
                    SyncMetadata(
                        tableName = config.tableName,
                        syncStatus = SyncMetadata.SyncStatus.COMPLETED,
                        lastSyncTime = System.currentTimeMillis(),
                        priority = config.priority
                    )
                )
            } catch (e: Exception) {
                database.syncMetadataDao().updateSyncMetadata(
                    SyncMetadata(
                        tableName = config.tableName,
                        syncStatus = SyncMetadata.SyncStatus.UNSYNCED,
                        lastSyncTime = System.currentTimeMillis(),
                        lastSyncError = e.message,
                        priority = config.priority
                    )
                )
                throw e
            }
        }
    }

    private suspend fun downloadChanges(config: TableSyncConfig) {
        val heavyTask = HeavySyncTask(
            tableName = config.tableName,
            taskConfig = HeavySyncTask.TaskConfig(
                networkLatencyMs = 1000L..4000L,
                cpuIntensity = 0.9f,
                memoryUsage = 10 * 1024 * 1024
            )
        )

        when (config.tableName) {
            "farmers" -> {
                val downloadedFarmers = mutableListOf<Farmer>()
                for (i in 0..5000) {
                    val farmer = Farmer(
                        name = "Farmer $i",
                        email = "farmer$i@gmail.com",
                        phoneNumber = "0901920192$i",
                        location = "Farmer $i Location",
                        specialtyCrops = "Farmer $i specialty",
                        profilePictureUrl = "Farmer $i profile picture",
                        syncStatus = SyncMetadata.SyncStatus.COMPLETED
                    )

                    heavyTask.execute(farmer.name)
                    downloadedFarmers.add(farmer)
                }

                database.farmerDao().updateFarmers(downloadedFarmers)
            }

            "crops" -> {
                val classOfFoods =
                    listOf("Carbs", "Protein", "Fats", "Minerals", "Vitamins", "Oils")
                val downloadedCrops = mutableListOf<Crop>()
                for (i in 0..5000) {
                    val crop = Crop(
                        name = "Crop $i",
                        classOfFood = classOfFoods.random(),
                        image = "https://example.com"
                    )

                    heavyTask.execute(crop.name)
                    downloadedCrops.add(crop)
                }

                database.cropDao().updateCrops(downloadedCrops)
            }

            "harvests" -> {
                val downloadedHarvests = mutableListOf<Harvest>()

                for (i in 0..5000) {
                    val harvest = Harvest(
                        cropName = "Harvest $i",
                        duration = "4 years",
                    )

                    heavyTask.execute(harvest.cropName)
                    downloadedHarvests.add(harvest)
                }

                database.harvestDao().updateHarvests(downloadedHarvests)
            }
        }
    }

    private suspend fun uploadChanges(config: TableSyncConfig) {
        var hasMoreData = true
        val heavyTask = HeavySyncTask(
            tableName = config.tableName,
            taskConfig = HeavySyncTask.TaskConfig(
                networkLatencyMs = 1000L..4000L,
                cpuIntensity = 0.9f,
                memoryUsage = 10 * 1024 * 1024
            )
        )

        while (hasMoreData) {
            val unsynced = getUnsyncedRecords<Any>(
                config.tableName,
                config.batchSize
            )

            if (unsynced.isEmpty()) {
                hasMoreData = false
                continue
            }

            when (config.tableName) {
                "farmers" -> {
                    val synced = (unsynced as List<Farmer>).map { e ->
                        // Simulate a long running task like api call, data serialization,
                        // file conversion on each data
                        heavyTask.execute(e.name)
                        e.copy(
                            syncStatus = SyncMetadata.SyncStatus.COMPLETED
                        )
                    }
                    database.farmerDao().updateFarmers(synced)
                }

                "crops" -> {
                    val synced = (unsynced as List<Crop>).map { e ->
                        heavyTask.execute(e. name)
                        e.copy(
                            syncStatus = SyncMetadata.SyncStatus.COMPLETED
                        )
                    }
                    database.cropDao().updateCrops(synced)
                }

                "harvests" -> {
                    val synced = (unsynced as List<Harvest>).map { e ->
                        heavyTask.execute(e.cropName)
                        e.copy(
                            syncStatus = SyncMetadata.SyncStatus.COMPLETED
                        )
                    }
                    database.harvestDao().updateHarvests(synced)
                }
            }
        }
    }
}