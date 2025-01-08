package com.farmbase.app.ui.farmerregistration

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.BuildConfig
import com.farmbase.app.models.Farmer
import com.farmbase.app.network.ImageUploadApi
import com.farmbase.app.repositories.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class FarmerRegistrationViewModel @Inject constructor(
    private val repository: FarmerRepository,
    private val imageUploadApi: ImageUploadApi,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _validationState = MutableStateFlow<ValidationResult?>(null)
    val validationState: StateFlow<ValidationResult?> = _validationState.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Initial)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    fun submitFarmer(
        name: String,
        email: String,
        phoneNumber: String,
        location: String,
        specialtyCrops: String,
        profilePictureUri: Uri?,
        farmerId: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val validationResult =
                    validateFarmerData(name, email, phoneNumber, location, specialtyCrops)
                if (validationResult !is ValidationResult.Success) {
                    _validationState.value = validationResult
                    return@launch
                }

                _uploadState.value = UploadState.Loading

                // Upload image if available
                val imageUrl = if (profilePictureUri != null && !profilePictureUri.toString().startsWith("http")) {
                    uploadImage(profilePictureUri)
                } else {
                    profilePictureUri.toString()
                }

                val farmer = Farmer(
                    id = farmerId ?: 0,
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                    location = location,
                    specialtyCrops = specialtyCrops,
                    profilePictureUrl = imageUrl
                )

                if (farmerId != null) {
                    repository.updateFarmer(farmer)
                } else {
                    repository.insertFarmer(farmer)
                }

                _uploadState.value = UploadState.Success
                _validationState.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun clearValidation() {
        _validationState.value = null
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
        } else {
            val nigerianPhoneRegex = "^(0|\\+?234)[789][01]\\d{8}$".toRegex()
            if (!nigerianPhoneRegex.matches(phoneNumber)) {
                errors.add("Please enter a valid Nigerian phone number")
            }
        }
        if (location.isBlank()) {
            errors.add("Location is required")
        }
        if (specialtyCrops.isBlank()) {
            errors.add("Specialty crops are required")
        }

        return if (errors.isEmpty()) ValidationResult.Success else ValidationResult.Error(errors)
    }

    private suspend fun uploadImage(uri: Uri): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IOException("Could not open input stream")

            val requestBody = inputStream.use { stream ->
                val bytes = stream.readBytes()
                RequestBody.create("image/*".toMediaType(), bytes)
            }

            val part = MultipartBody.Part.createFormData(
                "file",
                "profile_picture.jpg",
                requestBody
            )

            val apiKeyBody = BuildConfig.IMG_HIPPO_API_KEY.toRequestBody("text/plain".toMediaType())

            val response = imageUploadApi.uploadImage(part, apiKeyBody)
            if (response.isSuccessful) {
                if (response.body()?.status == 200 && response.body()?.success == true)
                    return response.body()?.data?.url
                else
                    throw IOException(response.body()?.message)
            } else
                throw IOException("Image upload failed: ${response.errorBody()?.string()}")
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to upload image: ${e.message}")
        }
    }
}

sealed class UploadState {
    data object Initial : UploadState()
    data object Loading : UploadState()
    data object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val errors: List<String>) : ValidationResult()
}