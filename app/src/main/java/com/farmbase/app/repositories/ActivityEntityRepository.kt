package com.farmbase.app.repositories

import com.farmbase.app.database.ActivityEntityDao
import com.farmbase.app.models.ActivityEntity
import kotlinx.coroutines.flow.Flow

class ActivityEntityRepository(
    private val activityEntityDao: ActivityEntityDao,
) {
    suspend fun insertActivities(activities: List<ActivityEntity>) {
        activityEntityDao.insertActivities(activities)
    }

    fun getAllUnscheduledActivities(): Flow<List<ActivityEntity>> {
        return activityEntityDao.selectAllUnscheduledActivities()
    }

    suspend fun replaceActivities(activities: List<ActivityEntity>) {
        activityEntityDao.replaceActivities(activities)
    }
}