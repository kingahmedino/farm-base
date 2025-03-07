package com.farmbase.app.network

import com.farmbase.app.models.FormData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FormBuilderApiService {
    @GET("/form")
    suspend fun getFormData(
        @Query("page")
        page: String = "",
        @Query("pageSize")
        pageSize: String = ""
    ): FormApiResponse

    @GET("form/{id}")
    suspend fun getFormDataById(
        @Path("id") id: String
    ): SingleFormApiResponse
}

data class FormApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<FormData>
)

data class SingleFormApiResponse(
    val success: Boolean,
    val message: String,
    val data: FormData
)