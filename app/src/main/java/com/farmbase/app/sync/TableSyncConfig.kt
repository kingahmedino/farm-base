package com.farmbase.app.sync

import com.farmbase.app.models.SyncMetadata

data class TableSyncConfig(
    val tableName: String,
    val batchSize: Int,
    val priority: SyncPriority,
) {
    fun toSyncMetadata(): SyncMetadata {
        return SyncMetadata(
            tableName = this.tableName,
            batchSize = this.batchSize,
            priority = this.priority,
        )
    }

    enum class SyncPriority {
        HIGH, MEDIUM, LOW
    }
}