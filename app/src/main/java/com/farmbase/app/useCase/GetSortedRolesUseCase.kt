package com.farmbase.app.useCase

import com.farmbase.app.R
import com.farmbase.app.repositories.RoleRepository
import com.farmbase.app.utils.ActivityCardItem
import com.farmbase.app.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * Use case to fetch roles from the repository, sort them by the number of child portfolios (in descending order),
 * and map each role to an ActivityCardItem. The result is returned as a Flow.
 */
class GetSortedRolesUseCase @Inject constructor(
    private val roleRepository: RoleRepository
) {
    /**
     * Executes the use case to fetch, sort, and map roles to ActivityCardItem.
     * @return Flow of a list of ActivityCardItem sorted by the number of child portfolios.
     */
    fun execute(description: String?): Flow<List<ActivityCardItem>> {
        // fetch roles from the repository as a flow and transform them
        return roleRepository.getAllRoles()
            .mapNotNull { roles ->
                // sort roles by the number of items in the comma-separated childPortfolio string
                roles.sortedByDescending { role ->
                    role.childPortfolio.split(",").size
                }.map { role ->
                    // map each RoleEntity to an ActivityCardItem to be displayed in the UI
                    ActivityCardItem(
                        id = role.roleId,
                        icon = R.drawable.ic_my_schedule,
                        headerText = role.name,
                        descText = description,
                        activityType = Constants.ActivityType.PORTFOLIO_ACTIVITY
                    )
                }
            }
    }
}

