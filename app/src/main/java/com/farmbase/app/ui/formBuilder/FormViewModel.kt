package com.farmbase.app.ui.formBuilder

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.models.FormData
import com.farmbase.app.ui.formBuilder.utils.Resource
import com.farmbase.app.useCase.FormBuilderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val formBuilderUseCases: FormBuilderUseCases
) : ViewModel() {

    private val _formData = MutableStateFlow<Resource<FormData>>(Resource.Loading())
    val formData: StateFlow<Resource<FormData>> = _formData.asStateFlow()

    private val _formFieldStates = mutableStateMapOf<String, List<FormFieldState>>()
    val formFieldStates: SnapshotStateMap<String, List<FormFieldState>> = _formFieldStates

    init {
        fetchFormData()
    }

    private fun fetchFormData() {
        viewModelScope.launch {
            _formData.value = Resource.Loading()
            _formData.value = formBuilderUseCases.getFormDataUseCase()

            (_formData.value as? Resource.Success)?.data?.let { formData ->
                _formFieldStates.putAll(formBuilderUseCases.initializeFormStateUseCase(formData))
            }
        }
    }

    fun updateFormField(screenId: String, fieldState: FormFieldState) {
        // Update the field's value
        _formFieldStates[screenId] = _formFieldStates[screenId]?.map {
            if (it.id == fieldState.id) fieldState else it
        } ?: listOf(fieldState)

        // Update form data
        val currentFormData = (_formData.value as? Resource.Success)?.data ?: return
        _formData.value = Resource.Success(
            formBuilderUseCases.updateFormFieldUseCase(currentFormData, screenId, fieldState)
        )

        // Update visibility for all fields that might depend on the changed field
        _formFieldStates[screenId]?.forEach { stateToUpdate ->

            // If this field has visibility rules
            val visibility = stateToUpdate.visibility.value

            if (visibility != null) {
                // Find the field that controls this field's visibility
                val controllingField = _formFieldStates[screenId]?.find {
                    it.id == visibility.targetId
                }

                val shouldBeVisible = when (visibility.condition.lowercase()) {
                    "equals" -> controllingField?.value?.value.equals(visibility.value, ignoreCase = true)
                    "not_equals" -> !controllingField?.value?.value.equals(visibility.value, ignoreCase = true)
                    "contains" -> controllingField?.value?.value?.contains(visibility.value, ignoreCase = true) ?: false
                    "not_contains" -> !controllingField?.value?.value?.contains(visibility.value, ignoreCase = true)!!
                    else -> true
                }

                stateToUpdate.isVisible.value = shouldBeVisible

                // Update visibility and clear field if hidden
                if (!shouldBeVisible) {
                    stateToUpdate.value.value = ""
                    stateToUpdate.isError.value = false
                    stateToUpdate.isInteracted.value = false
                    stateToUpdate.selectedValues.clear()
                }
            }
        }
    }

    fun isCurrentScreenValid(screenId: String): Boolean =
        _formFieldStates[screenId]?.none {
            it.isError.value && it.isVisible.value  // Only check visible fields
        } ?: false

    fun getFormDataToBeUploaded(): String? {
        return (_formData.value as? Resource.Success)?.data?.let { formData ->
            Json.encodeToString(formBuilderUseCases.streamlineFormDataUseCase(formData))
        }
    }
}