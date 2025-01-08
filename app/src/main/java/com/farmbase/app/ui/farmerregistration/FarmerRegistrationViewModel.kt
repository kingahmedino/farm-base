package com.farmbase.app.ui.farmerregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.models.Farmer
import com.farmbase.app.repositories.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerRegistrationViewModel @Inject constructor(
    private val repository: FarmerRepository
) : ViewModel() {
    private val _validationState = MutableStateFlow<ValidationResult?>(null)
    val validationState: StateFlow<ValidationResult?> = _validationState.asStateFlow()

    fun submitFarmer(
        name: String,
        email: String,
        phoneNumber: String,
        location: String,
        specialtyCrops: String,
        farmerId: Int? = null
    ) {
        val validationResult = validateFarmerData(name, email, phoneNumber, location, specialtyCrops)
        _validationState.value = validationResult

        if (validationResult is ValidationResult.Success) {
            val farmer = Farmer(
                id = farmerId ?: 0,
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                location = location,
                specialtyCrops = specialtyCrops
            )

            viewModelScope.launch {
                if (farmerId != null) {
                    repository.updateFarmer(farmer)
                } else {
                    repository.insertFarmer(farmer)
                }
                _validationState.value = null
            }
        }
    }

    private fun validateFarmerData(
        name: String,
        email: String,
        phoneNumber: String,
        location: String,
        specialtyCrops: String
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("Name is required")
        }
        if (email.isBlank()) {
            errors.add("Email is required")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("Invalid email format")
        }
        if (phoneNumber.isBlank()) {
            errors.add("Phone number is required")
        }
        if (location.isBlank()) {
            errors.add("Location is required")
        }
        if (specialtyCrops.isBlank()) {
            errors.add("Specialty crops are required")
        }

        return if (errors.isEmpty()) ValidationResult.Success else ValidationResult.Error(errors)
    }

    fun clearValidation() {
        _validationState.value = null
    }
}

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val errors: List<String>) : ValidationResult()
}