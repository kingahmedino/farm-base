package com.farmbase.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmbase.app.models.RoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoles(roles: List<RoleEntity>)

    @Query("Select * from roles")
    fun selectAllRoles(): Flow<List<RoleEntity>>
}