package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A composable for selecting times with a time picker dialog.
 *
 * This component displays a text field that, when clicked, opens a time picker dialog.
 * The selected time is formatted according to the specified time format and passed back
 * through the onTimeSelected callback.
 *
 * @param modifier Modifier to be applied to the composable
 * @param label Text label for the field (currently unused in the implementation)
 * @param placeholder Text to display when no time is selected
 * @param timeFormat Format pattern for displaying the time (e.g., "hh:mm a" or "HH:mm")
 * @param onTimeSelected Callback that provides the formatted time string when a time is selected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    modifier: Modifier = Modifier,
    label: String = "Time",
    placeholder: String = "HH:MM",
    timeFormat: String,
    onTimeSelected: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }

    OutlinedTextField(
        value = selectedTime,
        onValueChange = { }, // Read-only field
        placeholder = { Text(placeholder) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Select time",
                modifier = Modifier.clickable { showTimePicker = true }
            )
        },
        readOnly = true,
        modifier = modifier.clickable { showTimePicker = true }
    )

    if (showTimePicker) {
        DialWithDialog(
            timeFormat = timeFormat,
            onConfirm = { timePickerState ->
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                    set(Calendar.SECOND, 0) // Optional: Set seconds to 0
                }
                val formattedTime = SimpleDateFormat(timeFormat, Locale.getDefault()).format(calendar.time)
                selectedTime = formattedTime
                onTimeSelected(formattedTime)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

/**
 * A composable that displays a time picker dial within a dialog.
 *
 * @param timeFormat Format pattern for interpreting 12/24 hour mode
 * @param onConfirm Callback that provides the TimePickerState when user confirms selection
 * @param onDismiss Callback invoked when user dismisses the dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialog(
    timeFormat: String,
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = timeFormat.contains("HH") // Use 24-hour format if "HH" is in the format
    )

    TimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(state = timePickerState)
    }
}

/**
 * A dialog wrapper for the time picker component.
 *
 * @param onDismiss Callback invoked when user dismisses the dialog
 * @param onConfirm Callback invoked when user confirms the time selection
 * @param content The content to display in the dialog (typically a TimePicker)
 */
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        text = { content() }
    )
}