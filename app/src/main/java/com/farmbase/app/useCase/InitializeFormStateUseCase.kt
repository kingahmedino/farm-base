package com.farmbase.app.useCase

import androidx.compose.runtime.mutableStateOf
import com.farmbase.app.models.FormData
import com.farmbase.app.ui.formBuilder.FormFieldState

class InitializeFormStateUseCase {
    operator fun invoke(formData: FormData): Map<String, List<FormFieldState>> {
        return formData.screens.associate { screen ->
            screen.id to screen.sections
                .flatMap { section ->
                    section.components.map { component ->
                        FormFieldState(
                            id = component.id,
                            value = mutableStateOf(component.answer.orEmpty()),
                            hint = component.hint ?: "",
                            isError = mutableStateOf(component.required && component.answer.isNullOrEmpty()),
                            isInteracted = mutableStateOf(false),
                            selectedFile = mutableStateOf(null),
                            isVisible = mutableStateOf(component.visibility == null),
                            visibility = mutableStateOf(component.visibility)
                        )
                    }
                }
        }
    }
}