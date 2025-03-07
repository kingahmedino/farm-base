package com.farmbase.app.ui.formBuilder

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farmbase.app.models.Visibility

/**
 * Data class representing the state of a form field.
 *
 * This class maintains the runtime state of a form field, including its current value,
 * validation state, user interaction, selected files, and visibility conditions.
 *
 * @param id Unique identifier for the form field
 * @param value Current text value of the field
 * @param hint error message to display
 * @param isError Flag indicating if the field currently has a validation error
 * @param selectedValues List of selected values for multi-choice fields
 * @param isInteracted Flag indicating if the user has interacted with this field
 * @param selectedFile Selected file information as a pair of Uri and filename
 * @param isVisible Flag indicating if the field is currently visible
 * @param visibility Visibility conditions that determine when this field should be shown or hidden
 */
data class FormFieldState(
    val id: String,
    val value: MutableState<String> = mutableStateOf(""),
    val hint: String,
    val isError: MutableState<Boolean> = mutableStateOf(false),
    val selectedValues: SnapshotStateList<String> = mutableStateListOf(), // For multi-choice
    val isInteracted: MutableState<Boolean> = mutableStateOf(false),
    val selectedFile: MutableState<Pair<Uri?, String?>?> = mutableStateOf(null),
    val isVisible: MutableState<Boolean> = mutableStateOf(true),
    val visibility: MutableState<Visibility?> = mutableStateOf(null)
)
