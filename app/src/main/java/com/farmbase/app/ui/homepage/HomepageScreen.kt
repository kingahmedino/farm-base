package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.BottomSheet
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageScreen(
    role: String,
    onBackButtonClicked: () -> Unit,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    LaunchedEffect(role) {
        viewModel.updateHistoryList(role)
    }

    val portfolioActivityList by viewModel.portfolioActivityList.collectAsStateWithLifecycle()
    val portfolioList by viewModel.portfolioList.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val activityList by viewModel.activityList.collectAsStateWithLifecycle()
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()

    val sections = listOf(
        stringResource(R.string.execute_user_portfolio_activities, role) to listOf(portfolioActivityList, portfolioList),
        stringResource(R.string.execute_user_activities, role) to listOf(activityList, historyList)
    )

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }



    BottomSheet(
        sheetState = sheetState,
        showBottomSheet = showBottomSheet,
        sheetColor = colorResource(R.color.light_yellow),
        headerText = "Work in Progress",
        descText = "This feature is still been worked on",
        textColor = colorResource(R.color.black_text),
        buttonColor = R.color.yellow,
        buttonTextColor = R.color.black_text,
        iconTint = R.color.black_text,
        onDismissRequest = { showBottomSheet = false },
        onButtonClick = { showBottomSheet = false }
    )


    Scaffold(
        modifier = Modifier,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), onBackClick = onBackButtonClicked) },
        bottomBar = {
            NextButton(
                onClick = { showBottomSheet = true },
                enabled = selectedActivityCard != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) { paddingValues ->
        HomepageDetailScreen(
            role = role,
            paddingValues = paddingValues,
            sections = sections,
            viewModel = viewModel
        )
    }
}
