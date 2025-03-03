package com.farmbase.app.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmbase.app.models.Crop
import com.farmbase.app.models.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {
    @Query("SELECT * FROM crops")
    fun getAllCrops(): Flow<List<Crop>>

    @Insert
    suspend fun insertCrop(crop: Crop)

    @Update
    suspend fun updateCrop(crop: Crop)

    @Query("SELECT * FROM crops WHERE id = :id")
    suspend fun getCropById(id: Int): Crop?

    @Query("SELECT * FROM crops ORDER BY id DESC")
    fun getPaginatedCrops(): PagingSource<Int, Crop>

    @Query("SELECT * FROM crops WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<Crop>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrops(crops: List<Crop>)

    @Update
    suspend fun updateCrops(crops: List<Crop>)

    @Query("DELETE FROM crops WHERE id IN (:ids)")
    suspend fun deleteCrops(ids: List<String>)

    @Query("SELECT COUNT(*) FROM crops")
    suspend fun getCount(): Int
}