package com.farmbase.app.ui.selectProgram

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.ui.widgets.activityCardSection


@Composable
fun ProgramDetailScreen(
    onNextButtonClicked: () -> Unit,
    viewModel:SelectProgramViewModel = hiltViewModel()
) {
    val programList by viewModel.programList.collectAsStateWithLifecycle()
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()){}
        },
        bottomBar = { NextButton(
            onClick = {
                viewModel.saveProgramIdToSharedPrefs()
                viewModel.saveData { onNextButtonClicked() }
                      },
            enabled = selectedActivityCard != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) }
    ) {paddingValues ->
        LazyColumn ( modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item{
                Text(
                    text = stringResource(R.string.select_program),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.select_program_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            activityCardSection(
                itemList = programList,
                isItemSelected = { selectedActivityCard == it },
                onItemSelected = viewModel::updateSelectedCard,
                shouldDividerShow = false
            )
        }

    }
}