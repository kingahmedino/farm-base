package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.models.SyncMetadata.SyncStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val duration: Long,
    val syncStatus: SyncStatus = SyncStatus.UNSYNCED,
)