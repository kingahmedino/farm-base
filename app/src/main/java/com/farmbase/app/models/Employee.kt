package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.models.SyncMetadata.SyncStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val location: String,
    val profilePicture: String?,
    val syncStatus: SyncStatus = SyncStatus.UNSYNCED,
)