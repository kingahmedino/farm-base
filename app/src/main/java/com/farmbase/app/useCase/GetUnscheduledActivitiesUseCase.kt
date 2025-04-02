package com.farmbase.app.useCase

import com.farmbase.app.R
import com.farmbase.app.repositories.ActivityEntityRepository
import com.farmbase.app.utils.ActivityCardItem
import com.farmbase.app.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * Use case to fetch roles from the repository, sort them by the number of child portfolios (in descending order),
 * and map each role to an ActivityCardItem. The result is returned as a Flow.
 */
class GetUnscheduledActivitiesUseCase @Inject constructor(
    private val activitiesRepository: ActivityEntityRepository
) {
    /**
     * Executes the use case to fetch, sort, and map roles to ActivityCardItem.
     * @return Flow of a list of ActivityCardItem sorted by the number of child portfolios.
     */
    fun execute(): Flow<List<ActivityCardItem>> {
        // fetch roles from the repository as a flow and transform them
        return activitiesRepository.getAllUnscheduledActivities()
            .mapNotNull { roles ->
                // Transform each RoleEntity to an ActivityCardItem
                roles.map { role ->
                    ActivityCardItem(
                        id = role.activityId,
                        icon = R.drawable.ic_my_schedule,
                        headerText = role.name,
                        activityType = Constants.ActivityType.ACTIVITY
                    )
                }
            }
    }
}

