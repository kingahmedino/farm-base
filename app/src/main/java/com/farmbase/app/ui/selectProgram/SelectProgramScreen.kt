package com.farmbase.app.ui.selectProgram

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.ui.formBuilder.utils.Resource
import com.farmbase.app.utils.ErrorScreen
import com.farmbase.app.utils.LoadingScreen

@Composable
fun SelectProgramScreen(
    onNextButtonClicked: () -> Unit = {},
    viewModel:SelectProgramViewModel = hiltViewModel()) {

    val programData by viewModel.programData.collectAsStateWithLifecycle()
    when (programData) {
        is Resource.Loading -> {
            LoadingScreen("Fetching program details")
        }

        is Resource.Success -> {
          ProgramDetailScreen(onNextButtonClicked)
        }

        is Resource.Error -> {
            ErrorScreen(
                message = "Unable to fetch program information"
            )
        }
    }

    val programConfig by viewModel.programConfig.collectAsStateWithLifecycle()

    when (programConfig) {
        is Resource.Loading -> {
            LoadingScreen("Fetching program configurations",
                modifier = Modifier.background(Color.Black.copy(alpha = 0.4f)))
        }

        is Resource.Success -> {}

        is Resource.Error -> {}
    }

}

