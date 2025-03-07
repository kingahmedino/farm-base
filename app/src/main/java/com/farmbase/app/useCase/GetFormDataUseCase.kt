package com.farmbase.app.useCase

import com.farmbase.app.models.FormData
import com.farmbase.app.repositories.FormBuilderRepository
import com.farmbase.app.ui.formBuilder.utils.Resource

class GetFormDataUseCase(private val repository: FormBuilderRepository) {
    suspend operator fun invoke(): Resource<FormData> {
        return try {
            repository.getFormData()
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}