package com.farmbase.app.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmbase.app.models.Equipment
import com.farmbase.app.models.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipments")
    fun getAllEquipments(): Flow<List<Equipment>>

    @Insert
    suspend fun insertEquipment(equipment: Equipment)

    @Update
    suspend fun updateEquipment(equipment: Equipment)

    @Query("SELECT * FROM equipments WHERE id = :id")
    suspend fun getEquipmentById(id: Int): Equipment?

    @Query("SELECT * FROM equipments ORDER BY id DESC")
    fun getPaginatedEquipments(): PagingSource<Int, Equipment>

    @Query("SELECT * FROM equipments WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<Equipment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipments(equipments: List<Equipment>)

    @Update
    suspend fun updateEquipments(equipments: List<Equipment>)

    @Query("DELETE FROM equipments WHERE id IN (:ids)")
    suspend fun deleteEquipments(ids: List<String>)

    @Query("SELECT COUNT(*) FROM equipments")
    suspend fun getCount(): Int
}