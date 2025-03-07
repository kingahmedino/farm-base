package com.farmbase.app.useCase

import com.farmbase.app.models.FormData
import com.farmbase.app.ui.formBuilder.FormFieldState

class UpdateFormFieldUseCase {
    operator fun invoke(
        currentFormData: FormData,
        screenId: String,
        fieldState: FormFieldState
    ): FormData {
        return currentFormData.copy(
            screens = currentFormData.screens.map { screen ->
                if (screen.id == screenId) {
                    screen.copy(
                        sections = screen.sections.map { section ->
                            section.copy(
                                components = section.components.map { component ->
                                    if (component.id == fieldState.id) {
                                        component.copy(answer = fieldState.value.value)
                                    } else component
                                }
                            )
                        }
                    )
                } else screen
            }
        )
    }
}