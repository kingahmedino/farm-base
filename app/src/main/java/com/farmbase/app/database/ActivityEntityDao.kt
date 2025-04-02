package com.farmbase.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmbase.app.models.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<ActivityEntity>)

    @Query("Select * from activities where scheduledActivityFlag = 0")
    fun selectAllUnscheduledActivities(): Flow<List<ActivityEntity>>
}