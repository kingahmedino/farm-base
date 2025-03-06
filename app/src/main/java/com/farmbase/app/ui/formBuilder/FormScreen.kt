package com.farmbase.app.ui.formBuilder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.farmbase.app.models.Screen

/**
 * A composable that renders an individual screen/page of the form.
 *
 * This component displays a form screen with its title, description, progress indicator,
 * and all form sections. It handles scrolling for screens with content that exceeds
 * the visible area.
 *
 * @param modifier Modifier to be applied to the composable
 * @param screen The Screen object containing the data for this form page
 * @param currentPage The current page number (1-based)
 * @param totalPages The total number of pages in the form
 */
@Composable
fun FormScreen(
    modifier: Modifier = Modifier,
    screen: Screen,
    currentPage: Int,
    totalPages: Int,
) {

    val scrollState = rememberScrollState()
    val currentProgress = currentPage.toFloat() / totalPages.toFloat()

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        //form header
        Text(
            text = screen.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = screen.description,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProgressBarWithPageInfo(progress = currentProgress, currentPage = currentPage, totalPages = totalPages)

        Spacer(modifier = Modifier.height(8.dp))

        screen.sections.forEach { section ->
            FormSection(
                section = section,
                screenId = screen.id,
            )
        }
    }
}

/**
 * A composable that displays form progress as a progress bar with page information.
 *
 * This component shows a linear progress indicator along with text displaying the current
 * page number and total pages.
 *
 * @param modifier Modifier to be applied to the composable
 * @param progress The current progress as a fraction between 0 and 1
 * @param currentPage The current page number (1-based)
 * @param totalPages The total number of pages in the form
 */
@Composable
fun ProgressBarWithPageInfo(modifier: Modifier = Modifier, progress: Float, currentPage: Int, totalPages: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Page $currentPage of $totalPages",
            style = MaterialTheme.typography.bodySmall
        )
    }
}