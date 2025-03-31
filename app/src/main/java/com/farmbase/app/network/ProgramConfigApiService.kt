package com.farmbase.app.network

import com.farmbase.app.models.ProgramConfig
import retrofit2.http.GET
import retrofit2.http.Path

interface ProgramConfigApiService{
    @GET("/program/download/{program_id}")
    suspend fun getProgramConfigByProgramId(
        @Path("program_id") programId: String
    ) : ProgramConfig
}