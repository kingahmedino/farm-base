package com.farmbase.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.farmbase.app.models.SyncMetadata

@Dao
interface SyncMetadataDao {
    @Query("SELECT * FROM syncMetadatas")
    fun getAllSyncMetadatas(): List<SyncMetadata>

    @Upsert
    suspend fun updateSyncMetadata(syncMetadata: SyncMetadata)

    @Query("SELECT * FROM syncMetadatas WHERE tableName = :tableName")
    suspend fun getSyncMetadataByTableName(tableName: String): SyncMetadata?

    @Query("SELECT * FROM syncMetadatas WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<SyncMetadata>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadatas(syncMetadatas: List<SyncMetadata>)

    @Update
    suspend fun updateSyncMetadatas(syncMetadatas: List<SyncMetadata>)

    @Query("DELETE FROM syncMetadatas WHERE tableName IN (:tableNames)")
    suspend fun deleteSyncMetadatas(tableNames: List<String>)

    @Query("SELECT COUNT(*) FROM syncMetadatas")
    suspend fun getCount(): Int
}