package com.farmbase.app.useCase

import com.farmbase.app.models.FormData
import com.farmbase.app.repositories.FormBuilderRepository
import com.farmbase.app.ui.formBuilder.utils.Resource

class GetFormDataByIdUseCase(private val repository: FormBuilderRepository) {
    suspend operator fun invoke(id: String): Resource<FormData> {
        return try {
            repository.getFormDataById(id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}