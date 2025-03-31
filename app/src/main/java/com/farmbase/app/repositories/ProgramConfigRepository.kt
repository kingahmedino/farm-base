package com.farmbase.app.repositories


import com.farmbase.app.models.ProgramConfig
import com.farmbase.app.network.ProgramConfigApiService

class ProgramConfigRepository(
    private val api: ProgramConfigApiService
) {

    suspend fun getFormDataById(id: String): ProgramConfig {
        return api.getProgramConfigByProgramId(id)
    }
}
