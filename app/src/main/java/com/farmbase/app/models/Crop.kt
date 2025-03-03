package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.models.SyncMetadata.SyncStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "crops")
data class Crop(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val classOfFood: String,
    val image: String?,
    val syncStatus: SyncStatus = SyncStatus.UNSYNCED,
)