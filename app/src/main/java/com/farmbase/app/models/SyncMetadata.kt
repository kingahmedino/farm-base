package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.sync.TableSyncConfig

@Entity(tableName = "syncMetadatas")
data class SyncMetadata(
    @PrimaryKey
    val tableName: String,
    val lastSyncTime: Long? = null,
    val syncStatus: SyncStatus = SyncStatus.UNSYNCED,
    val lastSyncError: String? = null,
    val batchSize: Int = 100,
    val priority: TableSyncConfig.SyncPriority // Higher priority tables sync first
) {
    enum class SyncStatus {
        UNSYNCED, IN_PROGRESS, COMPLETED
    }
}