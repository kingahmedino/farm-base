package com.farmbase.app.models

import kotlinx.serialization.Serializable

@Serializable
data class UploadFormData(
    val formId: String,
    val userRelatedData: String = "",
    val screens: List<UploadScreen>
)

@Serializable
data class UploadScreen(
    val id: String,
    val sections: List<UploadSection>
)

@Serializable
data class UploadSection(
    val id: String,
    val components: List<UploadComponent>
)

@Serializable
data class UploadComponent(
    val id: String,
    val label: String,
    val answer: String?
)