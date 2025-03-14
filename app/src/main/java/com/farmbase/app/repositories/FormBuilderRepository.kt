package com.farmbase.app.repositories

import com.farmbase.app.models.FormData
import com.farmbase.app.models.UploadFormData
import com.farmbase.app.network.FormBuilderApiService
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
        return try {
            val response = api.getFormDataById(id)
            if (response.success) {
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown Error")
        }
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
