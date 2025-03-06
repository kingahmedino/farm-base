package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * A composable that provides an autocomplete text field with dropdown suggestions.
 *
 * This component displays a text field that shows a dropdown of matching suggestions as the user types.
 * Users can either type their input or select from the filtered suggestions list.
 *
 * @param modifier Modifier to be applied to the composable
 * @param suggestions List of strings to show as autocomplete suggestions
 * @param placeholder Text to display when the field is empty
 * @param selectedValue Current value of the text field
 * @param label Optional label for the text field
 * @param onValueChanged Callback that provides the new value when user types or selects a suggestion
 * @param isError Boolean indicating if the field is in an error state
 */
@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    suggestions: List<String>,
    placeholder: String = "",
    selectedValue: String,
    label: String = "",
    onValueChanged: (String) -> Unit,
    isError: Boolean = false
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { expanded = false }
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomOutlineTextField(
                value = selectedValue,
                onValueChange = {
                    onValueChanged(it)
                    expanded = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                placeholder = placeholder,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.ArrowDropDown,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                },
                label = label,
                singleLine = true,
                isError = isError
            )

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 264.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        val filteredItems = if (selectedValue.isNotEmpty()) {
                            suggestions.filter {
                                it.lowercase().contains(selectedValue.lowercase())
                            }.sorted()
                        } else {
                            suggestions.sorted()
                        }

                        if (filteredItems.isEmpty()) {
                            item {
                                Text(
                                    "No matches found",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            items(filteredItems) { item ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        onValueChanged(item)
                                        expanded = false
                                    },
                                    color = MaterialTheme.colorScheme.surfaceContainer
                                ) {
                                    Text(
                                        text = item,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}