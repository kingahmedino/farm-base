package com.farmbase.app.ui.formBuilder

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.farmbase.app.models.Section

/**
 * A composable that renders a section of a form with its components.
 *
 * This component displays a form section with a title, divider, and all form items within
 * the section. Each section is visually separated and contains grouped form fields.
 *
 * @param modifier Modifier to be applied to the composable
 * @param section The Section object containing the data for this form section
 * @param screenId The ID of the current screen/page in the form
 */
@Composable
fun FormSection(
    modifier: Modifier = Modifier,
    section: Section,
    screenId: String,
) {
    val backgroundColor = if (isSystemInDarkTheme()) Color.White else Color.LightGray
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        HorizontalDivider(thickness = 1.dp, color = backgroundColor)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = section.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Black
        )

        section.components.forEach { component ->
                FormItem(
                    elementData = component,
                    screenId = screenId,
                )
        }
    }
}