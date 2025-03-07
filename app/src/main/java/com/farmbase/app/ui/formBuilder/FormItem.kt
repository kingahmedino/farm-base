package com.farmbase.app.ui.formBuilder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.models.Country
import com.farmbase.app.models.FormElementData
import com.farmbase.app.models.FormInputType
import com.farmbase.app.ui.formBuilder.utils.AutoCompleteTextField
import com.farmbase.app.ui.formBuilder.utils.CustomOutlineTextField
import com.farmbase.app.ui.formBuilder.utils.DatePickerField
import com.farmbase.app.ui.formBuilder.utils.DropdownField
import com.farmbase.app.ui.formBuilder.utils.FileTagItem
import com.farmbase.app.ui.formBuilder.utils.Rating
import com.farmbase.app.ui.formBuilder.utils.TimePickerField
import com.farmbase.app.ui.formBuilder.utils.TooltipIcon
import com.farmbase.app.ui.formBuilder.utils.UploadFile
import com.farmbase.app.ui.formBuilder.utils.audio.AudioScreen
import com.farmbase.app.ui.formBuilder.utils.camera.CameraScreen
import com.farmbase.app.ui.formBuilder.utils.ValidationUtils

/**
 * A composable that renders a form field based on the provided element data.
 *
 * This component is the core building block of the form, handling the rendering and state management
 * for various input types (text fields, dropdowns, date pickers, file uploads, etc.). It manages
 * field validation, visibility, and interaction state.
 *
 * @param elementData The data describing the form element to render
 * @param screenId The ID of the current screen/page in the form
 * @param modifier Modifier to be applied to the composable
 * @param viewModel The ViewModel that manages form state and business logic
 */
@Composable
fun FormItem(
    elementData: FormElementData,
    screenId: String,
    modifier: Modifier = Modifier,
    viewModel: FormViewModel = hiltViewModel()
) {

    val fieldState = viewModel.formFieldStates[screenId]?.find { it.id == elementData.id }

    if (fieldState == null) {
        Text("Error: Missing field state for element ID ${elementData.id}")
        return
    }

    var isVisible by remember { mutableStateOf(fieldState.isVisible.value) }

    LaunchedEffect(fieldState.isVisible.value) {
        isVisible = fieldState.isVisible.value
    }

    if (!isVisible) return


    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputLabel(elementData)

            when (elementData.type) {
                FormInputType.SHORT_ANSWER -> {
                    CustomOutlineTextField(
                        value = fieldState.value.value,
                        onValueChange = { newValue ->
                            fieldState.value.value = newValue
                            fieldState.isInteracted.value = true

                            fieldState.isError.value = when {
                                !elementData.required && newValue.isEmpty() -> false
                                elementData.required && newValue.isEmpty() -> true
                                else -> {
                                    !ValidationUtils.validateInput(
                                        value = newValue,
                                        validation = elementData.validation,
                                        formElementData = elementData
                                    )
                                }
                            }

                            viewModel.updateFormField(screenId, fieldState)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = elementData.placeHolder ?: "",
                        singleLine = true,
                        isError = fieldState.isInteracted.value && fieldState.isError.value
                    )
                }

                FormInputType.PARAGRAPH -> {
                    CustomOutlineTextField(
                        value = fieldState.value.value,
                        onValueChange = { newValue ->
                            fieldState.value.value = newValue
                            fieldState.isInteracted.value = true

                            fieldState.isError.value = when {
                                !elementData.required && newValue.isEmpty() -> false
                                elementData.required && newValue.isEmpty() -> true
                                else -> {
                                    !ValidationUtils.validateInput(
                                        value = newValue,
                                        validation = elementData.validation,
                                        formElementData = elementData
                                    )
                                }
                            }

                            viewModel.updateFormField(screenId, fieldState)

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = elementData.placeHolder ?: "",
                        isError = fieldState.isInteracted.value && fieldState.isError.value,
                    )
                }

                FormInputType.NUMBER -> {
                    CustomOutlineTextField(
                        value = fieldState.value.value,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\$"))) {
                                fieldState.value.value = newValue
                                fieldState.isInteracted.value = true

                                fieldState.isError.value = when {
                                    !elementData.required && newValue.isEmpty() -> false
                                    elementData.required && newValue.isEmpty() -> true
                                    else -> !ValidationUtils.validateInput(
                                        value = newValue,
                                        validation = elementData.validation,
                                        formElementData = elementData
                                    )
                                }

                                viewModel.updateFormField(screenId, fieldState)

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = elementData.placeHolder ?: "",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = fieldState.isInteracted.value && fieldState.isError.value,
                    )
                }

                FormInputType.PHONE_NUMBER -> {
                    CustomOutlineTextField(
                        value = fieldState.value.value,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\$"))) {
                                fieldState.value.value = newValue
                                fieldState.isInteracted.value = true

                                fieldState.isError.value = when {
                                    !elementData.required && newValue.isEmpty() -> false
                                    elementData.required && newValue.isEmpty() -> true
                                    else -> !ValidationUtils.validateInput(
                                        value = newValue,
                                        validation = elementData.validation,
                                        formElementData = elementData
                                    )
                                }

                                viewModel.updateFormField(screenId, fieldState)

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = elementData.placeHolder ?: "",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        isError = fieldState.isInteracted.value && fieldState.isError.value,
                        leadingIcon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                if (elementData.countryCode != null) {
                                    Text(
                                        text = "TBD", //todo: make country code visible clickable to show other country codes
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "|",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        }
                    )
                }

                FormInputType.DATE -> {
                    DatePickerField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = elementData.placeHolder ?: "DD/MM/YYYY",
                        dateFormat = elementData.dateFormat ?: "dd/MM/yyyy",
                        minDate = elementData.minimumDate,
                        maxDate = elementData.maximumDate,
                        onDateSelected = { selectedDate ->
                            fieldState.isInteracted.value = true
                            fieldState.value.value = selectedDate
                            fieldState.isError.value = fieldState.isInteracted.value && elementData.required && selectedDate.isEmpty()

                            viewModel.updateFormField(screenId, fieldState)

                        }
                    )
                }

                FormInputType.TIME -> {
                    TimePickerField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = elementData.placeHolder ?: "HH:MM",
                        timeFormat = elementData.timeFormat ?: "hh:mm aa",
                        onTimeSelected = { selectedTime ->
                            fieldState.isInteracted.value = true
                            fieldState.value.value = selectedTime
                            fieldState.isError.value =
                                fieldState.isInteracted.value && elementData.required && selectedTime.isEmpty()
                            viewModel.updateFormField(screenId, fieldState)

                        }
                    )
                }

                FormInputType.SINGLE_CHOICE -> {
                    DropdownField(
                        options = elementData.options.orEmpty(),
                        selectedValue = fieldState.value.value,
                        onValueChange = { selectedValue ->
                            fieldState.isInteracted.value = true
                            fieldState.value.value = selectedValue
                            fieldState.isError.value =
                                fieldState.isInteracted.value && elementData.required && selectedValue.isEmpty()
                            viewModel.updateFormField(screenId, fieldState)

                        },
                        isError = fieldState.isInteracted.value && fieldState.isError.value
                    )
                }

                FormInputType.MULTIPLE_CHOICE -> {
                    DropdownField(
                        options = elementData.options.orEmpty(),
                        selectedValue = "",
                        placeholder = elementData.placeHolder ?: "",
                        onValueChange = {},
                        isError = fieldState.isInteracted.value && fieldState.isError.value,
                        isMultiChoice = true,
                        selectedValues = fieldState.selectedValues,
                        onMultiChoiceChange = { newSelectedValues ->
                            fieldState.isInteracted.value = true
                            fieldState.selectedValues.clear()
                            fieldState.selectedValues.addAll(newSelectedValues)
                            fieldState.isError.value =
                                elementData.required && newSelectedValues.isEmpty()
                            viewModel.updateFormField(screenId, fieldState)
                        }
                    )
                }

                FormInputType.DROPDOWN -> {
                    AutoCompleteTextField(
                        selectedValue = fieldState.value.value,
                        suggestions = elementData.options ?: if(elementData.label == "Country") {
                            Country.getAllCountryNames()
                        } else {
                            emptyList()
                        },
                        onValueChanged = { selectedValue ->
                            fieldState.isInteracted.value = true
                            fieldState.value.value = selectedValue
                            fieldState.isError.value =
                                fieldState.isInteracted.value && elementData.required && selectedValue.isEmpty()
                            viewModel.updateFormField(screenId, fieldState)
                        },
                        isError = fieldState.isInteracted.value && fieldState.isError.value,
                        placeholder = elementData.placeHolder ?: ""
                    )
                }

                FormInputType.FILE -> {
                    if (fieldState.selectedFile.value == null) {
                        UploadFile(
                            title = elementData.placeHolder ?: "",
                            type = elementData.mediaType ?: "",
                            onClick = { uri, fileName ->
                                fieldState.selectedFile.value = Pair(uri, fileName)
                                fieldState.value.value = fileName
                                fieldState.isError.value = false
                            }
                        )
                    } else {
                        val (uri, fileName) = fieldState.selectedFile.value!!
                        FileTagItem(
                            type = elementData.mediaType ?: "",
                            title = fileName ?: "Unknown File",
                            onClose = {
                                fieldState.selectedFile.value = null
                                fieldState.value.value = ""
                                fieldState.isError.value = elementData.required && fieldState.selectedFile.value == null
                            }
                        )
                    }
                    viewModel.updateFormField(screenId, fieldState)
                }

                FormInputType.RATING -> {
                    Rating(
                        rating = fieldState.value.value.toDoubleOrNull() ?: 0.0,
                        maxRating = elementData.numbersOfRating ?: 0,
                        lowRatingText = elementData.lowestRating ?: "",
                        highRatingText = elementData.highestRating ?: "",
                        shapeColor = Color.Yellow,
                        onRatingChanged = { newRating ->
                            fieldState.isInteracted.value = true
                            fieldState.value.value = newRating.toString()
                            fieldState.isError.value =
                                elementData.required && newRating.isNaN()
                            viewModel.updateFormField(screenId, fieldState)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                FormInputType.IMAGE, FormInputType.VIDEO -> {
                    if (fieldState.selectedFile.value == null) {
                        CameraScreen(
                            title = elementData.placeHolder ?: "",
                            type = elementData.type.toString().lowercase(),
                            onMediaCaptured = { uri, fileName ->
                                fieldState.selectedFile.value = Pair(uri, fileName)
                                fieldState.value.value = fileName
                                fieldState.isError.value = false
                            }
                        )
                    } else {
                        val fileName = fieldState.value.value
                        FileTagItem(
                            type = elementData.type.toString().lowercase(),
                            title = fileName ?: "Unknown File",
                            onClose = {
                                fieldState.selectedFile.value = null
                                fieldState.value.value = ""
                                fieldState.isError.value = elementData.required
                            }
                        )
                    }
                    viewModel.updateFormField(screenId, fieldState)
                }

                FormInputType.AUDIO_RECORDING -> {
                    if (fieldState.selectedFile.value == null) {
                        AudioScreen(
                            title = elementData.placeHolder ?: "",
                            onClick = { uri, fileName ->
                                fieldState.selectedFile.value = Pair(uri, fileName)
                                fieldState.value.value = fileName
                                fieldState.isError.value = false
                            }
                        )
                    } else {
                        val fileName = fieldState.value.value
                        FileTagItem(
                            type = elementData.type.toString().lowercase(),
                            title = fileName ?: "Unknown File",
                            onClose = {
                                fieldState.selectedFile.value = null
                                fieldState.value.value = ""
                                fieldState.isError.value = elementData.required
                            }
                        )
                    }
                }

                else -> {}
            }

            if ((fieldState.isInteracted.value && fieldState.isError.value)) {
                Text(
                    text = fieldState.hint,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * A composable that renders the label for a form input field.
 *
 * This component displays the field label with an optional required indicator (asterisk)
 * and tooltip icon for additional information.
 *
 * @param elementData The data describing the form element
 */
@Composable
private fun InputLabel(elementData: FormElementData) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                append(elementData.label)
                if (elementData.required) {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                        append(" *")
                    }
                }
            } ,
            style = MaterialTheme.typography.bodyMedium
        )

        if (!elementData.toolTip.isNullOrEmpty()) {
            TooltipIcon(toolTipText = elementData.toolTip)
        }
    }
}