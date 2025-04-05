package com.farmbase.app.repositories

import com.farmbase.app.models.FormData
import com.farmbase.app.models.UploadFormData
import com.farmbase.app.network.FormBuilderApiService
import com.farmbase.app.network.SingleFormApiResponse
import com.farmbase.app.network.ktor.KtorClient
import com.farmbase.app.ui.formBuilder.utils.Resource

class FormBuilderRepository(
    private val api: FormBuilderApiService
) {
    suspend fun getFormData(): Resource<FormData> {
        return try {
            val response = api.getFormData("1", "1")

            if (response.success && response.data.isNotEmpty()) {
                Resource.Success(response.data.first())
            } else {
                Resource.Error(response.message)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun getFormDataById(id: String): Resource<FormData> {
        return KtorClient.get<SingleFormApiResponse>("https://form-builder-service-dev-v25.agric-os.com/form/$id")
            .fold(
                onSuccess = { value -> Resource.Success(value.data) },
                onFailure = { e ->
                    e.printStackTrace()
                    Resource.Error(e.message ?: "Unknown error occurred")
                }
            )
    }

    suspend fun uploadFormData(uploadFormData: UploadFormData): Result<String> {
        return try {
            val response = api.uploadFormData(uploadFormData)
            if (response.success) {
                Result.success("")
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
