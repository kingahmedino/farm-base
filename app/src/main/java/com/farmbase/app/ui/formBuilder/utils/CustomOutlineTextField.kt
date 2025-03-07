package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A customized OutlinedTextField composable with consistent styling.
 *
 * This component wraps the standard OutlinedTextField with predefined styling and configuration
 * options for consistent appearance across the application.
 *
 * @param modifier Modifier to be applied to the composable
 * @param label Text label for the field (currently unused in the implementation)
 * @param value Current text value of the field
 * @param onValueChange Callback that provides the new value when the text changes
 * @param isError Boolean indicating if the field is in an error state
 * @param placeholder Text to display when the field is empty
 * @param singleLine Boolean to determine if the field allows multiple lines
 * @param leadingIcon Optional composable to display at the start of the text field
 * @param trailingIcon Optional composable to display at the end of the text field
 * @param keyboardOptions Options that specify text field behavior related to the keyboard
 * @param keyboardActions Actions to be taken when keyboard action buttons are pressed
 * @param readOnly Boolean indicating if the text field is read-only
 */
@Composable
fun CustomOutlineTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    placeholder: String,
    singleLine: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        isError = isError,
        placeholder = { Text(placeholder) },
        singleLine = singleLine,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        readOnly = readOnly
    )
}