package com.farmbase.app.ui.formBuilder.utils

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A composable for selecting dates with a date picker dialog.
 *
 * This component displays a text field that, when clicked, opens a date picker dialog.
 * The selected date is formatted according to the specified date format and passed back
 * through the onDateSelected callback.
 *
 * @param modifier Modifier to be applied to the composable
 * @param label Text label for the field (currently unused in the implementation)
 * @param placeholder Text to display when no date is selected
 * @param dateFormat Format pattern for displaying the date (e.g., "dd/MM/yyyy")
 * @param minDate Optional minimum selectable date in the same format as dateFormat
 * @param maxDate Optional maximum selectable date in the same format as dateFormat
 * @param onDateSelected Callback that provides the formatted date string when a date is selected
 */
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "DD/MM/YYYY",
    dateFormat: String,
    minDate: String? = null,
    maxDate: String? = null,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { }, // Read-only field
        placeholder = { Text(placeholder) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                modifier = Modifier.clickable {
                    showDatePicker(context, dateFormat, minDate, maxDate) { date ->
                        selectedDate = date
                        onDateSelected(date)
                    }
                }
            )
        },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                showDatePicker(context, dateFormat, minDate, maxDate) { date ->
                    selectedDate = date
                    onDateSelected(date)
                }
            }
    )
}

/**
 * Shows a DatePickerDialog with the specified configuration.
 *
 * @param context Android context used to create the dialog
 * @param dateFormat Format pattern for the returned date string
 * @param minDate Optional minimum selectable date
 * @param maxDate Optional maximum selectable date
 * @param onDateSelected Callback that provides the formatted date string when a date is selected
 */
fun showDatePicker(
    context: Context,
    dateFormat: String,
    minDate: String? = null,
    maxDate: String? = null,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val calendarInstance = Calendar.getInstance()
            calendarInstance.set(year, month, dayOfMonth)
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            val formattedDate = simpleDateFormat.format(calendarInstance.time)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Parse and set minDate
    minDate?.let {
        val parsedMinDate = parseDate(it, dateFormat)
        parsedMinDate?.let { datePickerDialog.datePicker.minDate = it.time }
    }

    // Parse and set maxDate
    maxDate?.let {
        val parsedMaxDate = parseDate(it, dateFormat)
        parsedMaxDate?.let { datePickerDialog.datePicker.maxDate = it.time }
    }

    datePickerDialog.show()
}

/**
 * Parses a date string into a Date object using the specified format.
 *
 * @param date The date string to parse
 * @param format The format pattern to use for parsing
 * @return The parsed Date object, or null if parsing fails
 */
fun parseDate(date: String, format: String): Date? {
    return try {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        simpleDateFormat.parse(date)
    } catch (e: Exception) {
        null
    }
}

