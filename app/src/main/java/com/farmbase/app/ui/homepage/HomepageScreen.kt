package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun HomepageScreen(
    role: String,
    onBackButtonClicked: () -> Unit,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    LaunchedEffect(role) {
        viewModel.updateRole(role)
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

    Scaffold(
        modifier = Modifier,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), onBackClick = onBackButtonClicked) },
        bottomBar = {
            NextButton(
                onClick = {},
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
