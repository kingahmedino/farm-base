package com.farmbase.app.network

import com.farmbase.app.models.ProgramConfig
import com.farmbase.app.models.ProgramData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProgramConfigApiService{
    @GET("/program/download/{program_id}")
    suspend fun getProgramConfigByProgramId(
        @Path("program_id") programId: String
    ) : ProgramConfigResponse

    @GET("program/download/roles-program")
    suspend fun getProgramDataByRoles(
        @Query("roles") roles: List<String>
    ): ProgramDataResponse
}

data class ProgramDataResponse(
    val status: Boolean,
    val message: String,
    val data: List<ProgramData>
)

data class ProgramConfigResponse(
    val status: Boolean,
    val message: String,
    val data: ProgramConfig
)