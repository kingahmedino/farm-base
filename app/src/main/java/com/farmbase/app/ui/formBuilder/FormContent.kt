package com.farmbase.app.ui.formBuilder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmbase.app.models.FormData
import kotlinx.coroutines.launch

/**
 * Main content composable for the form that handles navigation between form screens.
 *
 * This component displays the current form screen in a pager and provides navigation controls.
 * It handles back navigation, validation of the current screen, and submission of the
 * completed form.
 *
 * @param formData The form data containing all screens and form elements
 * @param onSaveAndUpload Callback invoked when the form is completed and submitted
 * @param isScreenValid Function that determines if the current screen has valid inputs
 */
@Composable
fun FormContent(
    formData: FormData,
    onSaveAndUpload: () -> Unit = {},
    isScreenValid: (String) -> Boolean
) {
    val pagerState = rememberPagerState(pageCount = { formData.totalScreens })
    val scope = rememberCoroutineScope()
    val currentScreenId = formData.screens[pagerState.currentPage].id
    val isNextButtonEnabled = isScreenValid(currentScreenId)

    BackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false,
        ) { page ->
            FormScreen(
                screen = formData.screens[page],
                currentPage = page + 1,
                totalPages = formData.totalScreens
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                scope.launch {
                    if (pagerState.currentPage + 1 < pagerState.pageCount) {
                        // Navigate to the next page
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        // last page
                        onSaveAndUpload()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isNextButtonEnabled
        ) {
            Text(
                text = if (pagerState.currentPage + 1 < pagerState.pageCount) "Next" else "Finish"
            )
        }
    }

}