package com.farmbase.app.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmbase.app.models.Storage
import com.farmbase.app.models.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageDao {
    @Query("SELECT * FROM storages")
    fun getAllStorages(): Flow<List<Storage>>

    @Insert
    suspend fun insertStorage(storage: Storage)

    @Update
    suspend fun updateStorage(storage: Storage)

    @Query("SELECT * FROM storages WHERE id = :id")
    suspend fun getStorageById(id: Int): Storage?

    @Query("SELECT * FROM storages ORDER BY id DESC")
    fun getPaginatedStorages(): PagingSource<Int, Storage>

    @Query("SELECT * FROM storages WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<Storage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorages(storages: List<Storage>)

    @Update
    suspend fun updateStorages(storages: List<Storage>)

    @Query("DELETE FROM storages WHERE id IN (:ids)")
    suspend fun deleteStorages(ids: List<String>)

    @Query("SELECT COUNT(*) FROM storages")
    suspend fun getCount(): Int
}