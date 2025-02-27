package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.models.SyncMetadata.SyncStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "farmers")
data class Farmer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val location: String,
    val specialtyCrops: String,
    val profilePictureUrl: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.UNSYNCED,
)