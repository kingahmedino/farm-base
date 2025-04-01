package com.farmbase.app.repositories


import com.farmbase.app.models.ProgramConfig
import com.farmbase.app.models.ProgramData
import com.farmbase.app.network.ProgramConfigApiService
import com.farmbase.app.ui.formBuilder.utils.Resource

class ProgramConfigRepository(
    private val api: ProgramConfigApiService
) {
    suspend fun getProgramConfigByProgramId(id: String): Resource<ProgramConfig> {
        return try {
            val response = api.getProgramConfigByProgramId(id)

            if (response.status) {
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?:"Error occurred while fetching program data")
        }
    }

    suspend fun getProgramDataByRoles(roles: List<String>): Resource<List<ProgramData>> {
        return try {
            val response = api.getProgramDataByRoles(roles)

            if (response.status && response.data.isNotEmpty()) {
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?:"Error occurred while fetching program data")
        }
    }
}
