package com.farmbase.app.ui.selectProgram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.ui.formBuilder.utils.Resource
import com.farmbase.app.utils.ErrorScreen
import com.farmbase.app.utils.LoadingScreen

@Composable
fun SelectProgramScreen(
    onNextButtonClicked: () -> Unit = {},
    viewModel:SelectProgramViewModel = hiltViewModel()) {

    val programConfig by viewModel.programData.collectAsStateWithLifecycle()
    when (programConfig) {
        is Resource.Loading -> {
            LoadingScreen()
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

}

