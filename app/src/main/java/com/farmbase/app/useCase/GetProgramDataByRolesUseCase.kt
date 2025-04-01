package com.farmbase.app.useCase

import com.farmbase.app.models.ProgramData
import com.farmbase.app.repositories.ProgramConfigRepository
import com.farmbase.app.ui.formBuilder.utils.Resource

class GetProgramDataByRolesUseCase(private val repository: ProgramConfigRepository) {
    suspend operator fun invoke(roles: List<String>): Resource<List<ProgramData>> {
        return try {
            repository.getProgramDataByRoles(roles)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}