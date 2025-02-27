package com.farmbase.app.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {
    @Query("SELECT * FROM farmers")
    fun getAllFarmers(): Flow<List<Farmer>>

    @Insert
    suspend fun insertFarmer(farmer: Farmer)

    @Update
    suspend fun updateFarmer(farmer: Farmer)

    @Query("SELECT * FROM farmers WHERE id = :id")
    suspend fun getFarmerById(id: Int): Farmer?

    @Query("SELECT * FROM farmers ORDER BY timestamp DESC")
    fun getPaginatedFarmers(): PagingSource<Int, Farmer>

    @Query("SELECT * FROM farmers WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<Farmer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarmers(farmers: List<Farmer>)

    @Update
    suspend fun updateFarmers(farmers: List<Farmer>)

    @Query("DELETE FROM farmers WHERE id IN (:ids)")
    suspend fun deleteFarmers(ids: List<String>)

    @Query("SELECT COUNT(*) FROM farmers")
    suspend fun getCount(): Int
}