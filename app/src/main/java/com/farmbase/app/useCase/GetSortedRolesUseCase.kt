package com.farmbase.app.useCase

import android.content.Context
import android.os.Environment
import android.util.Log
import com.farmbase.app.R
import com.farmbase.app.repositories.RoleRepository
import com.farmbase.app.utils.ActivityCardItem
import com.farmbase.app.utils.Constants
import com.farmbase.app.utils.SharedPreferencesManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import java.io.File
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
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(description: String?, context: Context): Flow<List<ActivityCardItem>> {
        val roleId = SharedPreferencesManager(context).encryptedGet(Constants.SELECTED_ROLE_ID)
        Log.d("Role_id", roleId.toString())
        // fetch roles from the repository as a flow and transform them
        return roleRepository.getChildPortfolioData(roleId ?: "")
            .flatMapLatest { roleIdsString ->
                // If roleIdsString is null or empty, return an empty flow
                Log.d("Roleid string", roleIdsString.toString())
                if (roleIdsString.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    roleIdsString.split(",")
                    roleRepository.getRolesById(roleIdsString.split(",").map { it.trim() })
                        .mapNotNull { roles ->
                            // sort roles by the number of items in the comma-separated childPortfolio string
                            roles.sortedByDescending { role ->
                                if (role.childPortfolio.isNullOrBlank()){ 0} else {
                                    role.childPortfolio.split(",").size
                                }
                            }.map { role ->

                                val directory = File(
                                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                    "Icons"
                                )
                                val filePath = File(directory, "${role.roleId}.jpg")
                                // map each RoleEntity to an ActivityCardItem to be displayed in the UI
                                ActivityCardItem(
                                    id = role.roleId,
                                    icon = R.drawable.ic_my_portfolio,
                                    headerText = role.name,
                                    descText = description,
                                    activityType = Constants.ActivityType.PORTFOLIO_ACTIVITY,
                                    iconFile = filePath
                                )
                            }
                        }
                }
            }
    }
}

