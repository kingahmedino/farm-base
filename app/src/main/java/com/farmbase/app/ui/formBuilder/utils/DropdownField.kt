package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable that provides both single and multi-choice dropdown selection functionality.
 *
 * This component displays a text field that, when clicked, opens a dropdown menu with available options.
 * It supports both single-selection mode where one option can be selected at a time, and
 * multi-selection mode where multiple options can be selected simultaneously using checkboxes.
 *
 * @param label Optional label text for the dropdown field
 * @param placeholder Text to display when no option is selected
 * @param options List of string options to display in the dropdown
 * @param selectedValue The currently selected value (for single-choice mode)
 * @param onValueChange Callback that provides the new selected value when an option is chosen in single-choice mode
 * @param isError Boolean indicating if the field is in an error state
 * @param isMultiChoice Boolean toggle between single-choice (false) and multi-choice (true) mode
 * @param selectedValues List of currently selected values (for multi-choice mode)
 * @param onMultiChoiceChange Callback that provides the updated list of selected values in multi-choice mode
 */
@Composable
fun DropdownField(
    label: String = "",
    placeholder: String = "",
    options: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isMultiChoice: Boolean = false,
    selectedValues: List<String> = emptyList(),
    onMultiChoiceChange: (List<String>) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        CustomOutlineTextField(
            value = if (isMultiChoice) {
                selectedValues.joinToString(", ")
            } else {
                selectedValue
            },
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            label = label,
            placeholder = placeholder,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown arrow",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            readOnly = true,
            isError = isError
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                if (isMultiChoice) {
                    val isSelected = option in selectedValues
                    DropdownMenuItem(
                        onClick = {
                            val updatedList = if (isSelected) {
                                selectedValues - option
                            } else {
                                selectedValues + option
                            }
                            onMultiChoiceChange(updatedList)
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = {
                                        val updatedList = if (it) {
                                            selectedValues + option
                                        } else {
                                            selectedValues - option
                                        }
                                        onMultiChoiceChange(updatedList)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(option)
                            }
                        }
                    )
                } else {
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        text = { Text(option) }
                    )
                }
            }
        }
    }
}

/**
 * Preview composable for the DropdownField.
 *
 * Demonstrates both single-choice and multi-choice dropdown fields with sample options.
 */
@Preview(showBackground = true)
@Composable
fun DropdownFieldPreview() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var singleChoiceValue by remember { mutableStateOf("") }
    val multiChoiceValues = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Single Choice Dropdown Preview
        DropdownField(
            label = "Single Choice",
            options = options,
            selectedValue = singleChoiceValue,
            onValueChange = { singleChoiceValue = it },
            isError = false
        )

        // Multi Choice Dropdown Preview
        DropdownField(
            placeholder = "Multi Choice",
            options = options,
            selectedValue = "",
            selectedValues = multiChoiceValues,
            onValueChange = {},
            isMultiChoice = true,
            onMultiChoiceChange = { updatedValues ->
                multiChoiceValues.clear()
                multiChoiceValues.addAll(updatedValues)
            },
            isError = false,
        )
    }
}

