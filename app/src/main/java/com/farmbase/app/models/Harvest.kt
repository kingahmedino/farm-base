package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.models.SyncMetadata.SyncStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "harvests")
data class Harvest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cropName: String,
    val duration: String,
    val timeOfHarvest: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.UNSYNCED,
)
