package com.farmbase.app.ui.selectProgram

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.ActivityCardList
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun SelectProgramScreen(
    onNextButtonClicked: () -> Unit = {},
    viewModel:SelectProgramViewModel = hiltViewModel()) {
    val buttonEnabled by viewModel.isButtonEnabled.collectAsState()
    val programList by viewModel.programList.collectAsState()

    Scaffold(modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()){}
        },
        bottomBar = { NextButton(
            onClick = {onNextButtonClicked()},
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) }
    ) {paddingValues ->
        LazyColumn ( modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .nestedScroll(rememberNestedScrollInteropConnection())
            .padding(start = 16.dp, end = 16.dp)
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
                Spacer(modifier = Modifier.height(12.dp))
//                ActivityCardList(programList) {
//                    viewModel.updateSelectedCard(it.first)
//                }

            }

        }

    }
}
