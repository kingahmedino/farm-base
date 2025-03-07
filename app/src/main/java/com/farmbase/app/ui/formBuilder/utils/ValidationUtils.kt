package com.farmbase.app.ui.formBuilder.utils

import android.util.Log
import com.farmbase.app.models.FormElementData
import com.farmbase.app.models.FormInputType
import com.farmbase.app.models.ValidationRule
import java.text.SimpleDateFormat
import java.util.Locale

object ValidationUtils {
    fun validateInput(
        value: String,
        validation: ValidationRule?,
        formElementData: FormElementData
    ): Boolean {
        if (formElementData.validate == false) return true

        return when (formElementData.type) {
            FormInputType.SHORT_ANSWER, FormInputType.PARAGRAPH -> {
                val wordCount = if (value.isBlank()) 0 else value.trim().split("\\s+".toRegex()).size
                val minWordCount = formElementData.minimumCharacterCount ?: 0
                val maxWordCount = formElementData.maximumCharacterCount ?: Int.MAX_VALUE

                val withinWordCount = wordCount in minWordCount..maxWordCount

                val matchesRegex = validation?.regex?.let { regex ->
                    value.matches(Regex(regex))
                } ?: true

                withinWordCount && matchesRegex
            }

            FormInputType.NUMBER -> {
                val number = value.toIntOrNull() ?: return false
                val minValue = formElementData.minimumValue ?: Int.MIN_VALUE
                val maxValue = formElementData.maximumValue ?: Int.MAX_VALUE
                number in minValue..maxValue
            }

            FormInputType.PHONE_NUMBER -> {
                validation?.regex?.let { regex ->
                    value.matches(Regex(regex))
                } ?: true
            }

            FormInputType.DATE -> {
                try {
                    val dateFormat = SimpleDateFormat(formElementData.dateFormat ?: "dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(value)
                    val minDate = formElementData.minimumDate?.let { dateFormat.parse(it) }
                    val maxDate = formElementData.maximumDate?.let { dateFormat.parse(it) }
                    Log.d("ValidationUtils", "date: $date")
                    Log.d("ValidationUtils", "minDate: $minDate, maxDate: $maxDate")
                    Log.d("validation", "min date ${minDate}")

                    when {
                        minDate != null && maxDate != null -> date in minDate..maxDate
                        minDate != null -> date >= minDate
                        maxDate != null -> date <= maxDate
                        else -> true
                    }
                } catch (e: Exception) {
                    false
                }
            }

            FormInputType.TIME -> true // check minimum and max duration
            else -> true   // Validation done at UI level
        }
    }
}