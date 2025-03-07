package com.farmbase.app.useCase

import com.farmbase.app.models.FormData
import com.farmbase.app.models.UploadComponent
import com.farmbase.app.models.UploadFormData
import com.farmbase.app.models.UploadScreen
import com.farmbase.app.models.UploadSection

class StreamlineFormDataUseCase {
    operator fun invoke(formData: FormData): UploadFormData {
        // Convert FormData to UploadFormData
        return UploadFormData(
            id = formData.id,
            userRelatedData = "logged in user data",
            screens = formData.screens.map { screen ->
                UploadScreen(
                    id = screen.id,
                    sections = screen.sections.map { section ->
                        UploadSection(
                            id = section.id,
                            components = section.components
                                .mapNotNull { component ->
                                    component.answer?.let {
                                        UploadComponent(
                                            id = component.id,
                                            label = component.label,
                                            answer = it
                                        )
                                    }
                                }
                        )
                    }
                )
            }
        )
    }
}