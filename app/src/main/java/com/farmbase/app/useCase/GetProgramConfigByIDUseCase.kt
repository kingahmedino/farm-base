package com.farmbase.app.useCase

import com.farmbase.app.models.ProgramConfig
import com.farmbase.app.repositories.ProgramConfigRepository
import com.farmbase.app.ui.formBuilder.utils.Resource

class GetProgramConfigByIDUseCase(private val repository: ProgramConfigRepository) {
    suspend operator fun invoke(id: String): Resource<ProgramConfig> {
        return try {
            Resource.Success(repository.getFormDataById(id))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}