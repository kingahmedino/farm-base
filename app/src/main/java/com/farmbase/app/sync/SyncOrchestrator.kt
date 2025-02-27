package com.farmbase.app.sync

import android.util.Log
import com.farmbase.app.database.FarmBaseDatabase
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.SyncMetadata
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
    )

    // Extension function to get unsynced records for any table
    private suspend inline fun <reified T> getUnsyncedRecords(
        tableName: String,
        batchSize: Int
    ): List<T> {
        return when (tableName) {
            "farmers" -> database.farmerDao().getUnsynced(limit = batchSize)
//            "projects" -> database.projectDao().getUnsynced(batchSize)
            else -> emptyList()
        } as List<T>
    }

    // Extension function to mark synced records for any table
    private suspend inline fun <reified T> markAsSynced(
        tableName: String,
        unSynced: List<T>
    ) {
        when (tableName) {
            "farmers" -> {
                val synced = (unSynced as List<Farmer>).map { e ->
                    e.copy(
                        syncStatus = SyncMetadata.SyncStatus.COMPLETED
                    )
                }
                database.farmerDao().updateFarmers(synced)
            }
        }
    }

    suspend fun startSync() {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        try {
            // Sort tables by priority and dependencies
            val prioritizedTables = tableConfigs.groupBy { it.priority }

            val highPriorityJobs = (prioritizedTables[TableSyncConfig.SyncPriority.HIGH] ?: emptyList()).map {
                coroutineScope.syncTable(it)
            }
            highPriorityJobs.awaitAll()

            val mediumPriorityJobs = (prioritizedTables[TableSyncConfig.SyncPriority.MEDIUM] ?: emptyList()).map {
                coroutineScope.syncTable(it)
            }
            mediumPriorityJobs.awaitAll()

            val lowPriorityJobs = (prioritizedTables[TableSyncConfig.SyncPriority.LOW] ?: emptyList()).map {
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

                // Download changes from server
                downloadChanges(config)

                // Upload local changes
                uploadChanges(config)

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
//        val lastSyncTime = database.syncMetadataDao().getSyncMetadataByTableName(config.tableName)?.lastSyncTime
//        var hasMoreData = true
//        var offset = 0
//
//        while (hasMoreData) {
//            val response = api.getChanges(
//                table = config.tableName,
//                lastSyncTime = lastSyncTime,
//                limit = config.batchSize,
//                offset = offset
//            )
//
//            if (response.isSuccessful && response.body() != null) {
//                val changes = response.body()!!
//                saveChangesToDatabase(config.tableName, changes)
//
//                hasMoreData = changes.size == config.batchSize
//                offset += config.batchSize
//            } else {
//                throw Exception("Failed to download changes for ${config.tableName}")
//            }
//        }
    }

    private suspend fun uploadChanges(config: TableSyncConfig) {
        var hasMoreData = true

        while (hasMoreData) {
            val unsynced = getUnsyncedRecords<Any>(
                config.tableName,
                config.batchSize
            )

            if (unsynced.isEmpty()) {
                hasMoreData = false
                continue
            }

            markAsSynced(config.tableName, unsynced)
        }
    }
}