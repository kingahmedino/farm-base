package com.farmbase.app.ui.formBuilder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.ui.formBuilder.utils.Resource

/**
 * The main form builder composable that manages form data state and rendering.
 *
 * This component observes the form data state from the ViewModel and displays the appropriate UI:
 * loading indicator, form content, or error message based on the current state.
 *
 * @param modifier Modifier to be applied to the composable
 * @param viewModel The ViewModel that provides form data and business logic
 */
@Composable
fun FormBuilder(
    modifier: Modifier = Modifier,
    viewModel: FormViewModel = hiltViewModel()
) {
    val formDataState by viewModel.formData.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (formDataState) {
                is Resource.Loading -> {
                    LoadingScreen()
                }

                is Resource.Success -> {
                    formDataState.data?.let { formData ->
                        FormContent(
                            formData = formData,
                            onSaveAndUpload = {
                                viewModel.saveForm()
                            },
                            isScreenValid = { screenId ->
                                viewModel.isCurrentScreenValid(screenId)
                            }
                        )
                    } ?: ErrorScreen(message = "No form data available")
                }

                is Resource.Error -> {
                    ErrorScreen(
                        message = formDataState.message ?: "An error occurred"
                    )
                }
            }
        }
    }

}

/**
 * A loading screen that displays a centered circular progress indicator.
 */
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * An error screen that displays a centered error message.
 *
 * @param message The error message to display
 */
@Composable
private fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}