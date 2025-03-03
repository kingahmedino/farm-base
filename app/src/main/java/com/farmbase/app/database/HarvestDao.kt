package com.farmbase.app.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmbase.app.models.Harvest
import com.farmbase.app.models.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface HarvestDao {
    @Query("SELECT * FROM harvests")
    fun getAllHarvests(): Flow<List<Harvest>>

    @Insert
    suspend fun insertHarvest(harvest: Harvest)

    @Update
    suspend fun updateHarvest(harvest: Harvest)

    @Query("SELECT * FROM harvests WHERE id = :id")
    suspend fun getHarvestById(id: Int): Harvest?

    @Query("SELECT * FROM harvests ORDER BY timeOfHarvest DESC")
    fun getPaginatedHarvests(): PagingSource<Int, Harvest>

    @Query("SELECT * FROM harvests WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<Harvest>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvests(harvests: List<Harvest>)

    @Update
    suspend fun updateHarvests(harvests: List<Harvest>)

    @Query("DELETE FROM harvests WHERE id IN (:ids)")
    suspend fun deleteHarvests(ids: List<String>)

    @Query("SELECT COUNT(*) FROM harvests")
    suspend fun getCount(): Int
}