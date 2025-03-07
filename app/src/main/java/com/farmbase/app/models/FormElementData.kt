package com.farmbase.app.models

import com.google.gson.annotations.SerializedName

data class FormData(
    val id: String,
    val name: String,
    val description: String,
    val totalScreens: Int,
    val screens: List<Screen>
)

data class AudioData(
    val name: String,
    val url: String,
    val format: String
)

data class Screen(
    val id: String,
    val name: String,
    val description: String,
    val sections: List<Section>
)

data class Section(
    val id: String,
    val title: String,
    val components: List<FormElementData>
)

data class FormElementData(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: FormInputType,
    @SerializedName("label")
    val label: String,
    @SerializedName("hint")
    val hint: String?,
    @SerializedName("tooltip")
    val toolTip: String?,
    @SerializedName("required")
    val required: Boolean = false,
    @SerializedName("minimumCharacterCount")
    val minimumCharacterCount: Int? = null,
    @SerializedName("maximumCharacterCount")
    val maximumCharacterCount: Int? = null,
    @SerializedName("minimumValue")
    val minimumValue: Int? = null,
    @SerializedName("maximumValue")
    val maximumValue: Int? = null,
    @SerializedName("currency")
    val currency: Boolean? = null,
    @SerializedName("countryCode")
    val countryCode: CountryCode? = null,
    @SerializedName("options")
    val options: List<String>? = null,
    @SerializedName("validate")
    val validate: Boolean? = false,
    @SerializedName("validation")
    val validation: ValidationRule? = null,
    @SerializedName("visibility")
    val visibility: Visibility? = null,
    @SerializedName("minimumFileSize")
    val minimumFileSize: Int? = null,
    @SerializedName("maximumFileSize")
    val maximumFileSize: Int? = null,
    @SerializedName("allowMultipleFiles")
    val allowMultipleFiles: Boolean? = null,
    @SerializedName("file")
    val file: File? = null,
    @SerializedName("minimumDuration")
    val minimumDuration: String? = null,
    @SerializedName("maximumDuration")
    val maximumDuration: String? = null,
    @SerializedName("minimumDate")
    val minimumDate: String? = null,
    @SerializedName("maximumDate")
    val maximumDate: String? = null,
    @SerializedName("addOther")
    val addOther: Boolean? = false,
    @SerializedName("addCheckAll")
    val addCheckAll: Boolean? = false,
    @SerializedName("numbersOfRating")
    val numbersOfRating: Int? = null,
    @SerializedName("lowestRating")
    val lowestRating: String? = null,
    @SerializedName("highestRating")
    val highestRating: String? = null,
    @SerializedName("optionType")
    val optionType: String? = null,
    @SerializedName("dateFormat")
    val dateFormat: String? = null,
    @SerializedName("timeFormat")
    val timeFormat: String? = null,
    @SerializedName("answer")
    val answer: String? = null,
    @SerializedName("placeholder")
    val placeHolder: String?,
    @SerializedName("mediaType")
    val mediaType: String?,
)

data class CountryCode(
    val regex: String? = null,
)//todo: need to validate on country code

data class Visibility(
    val condition: String,
    val targetId: String,
    val value: String
)

data class ValidationRule(
    val regex: String? = null,
)

data class File(
    val type: String,
    val formats: List<String>
)

enum class FormInputType {
    @SerializedName("short_answer")
    SHORT_ANSWER,

    @SerializedName("paragraph")
    PARAGRAPH,

    @SerializedName("number")
    NUMBER,

    @SerializedName("phone_number")
    PHONE_NUMBER,

    @SerializedName("date")
    DATE,

    @SerializedName("time")
    TIME,

    @SerializedName("dropdown")
    DROPDOWN,

    @SerializedName("single_choice")
    SINGLE_CHOICE,

    @SerializedName("multiple_choice")
    MULTIPLE_CHOICE,

    @SerializedName("file")
    FILE,

    @SerializedName("camera")
    CAMERA,

    @SerializedName("audio_recording")
    AUDIO_RECORDING,

    @SerializedName("rating")
    RATING,

    @SerializedName("image")
    IMAGE,

    @SerializedName("video")
    VIDEO,

    @SerializedName("audio")
    AUDIO
}