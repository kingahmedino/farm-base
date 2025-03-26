package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun HomepageDetailScreen(
    role: String,
    onBackButtonClicked: () -> Unit,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    viewModel.updateRole(role)

    val portfolioActivityList by viewModel.portfolioActivityList.collectAsStateWithLifecycle()
    val portfolioList by viewModel.portfolioList.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val activityList by viewModel.activityList.collectAsStateWithLifecycle()
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()
    val roles by viewModel.role.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()) { onBackButtonClicked() }
        },
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HomepageHeader(role, showDialog, onDialogDismiss = { showDialog = false }) }

            item { ActivityCardTitle(title = stringResource(R.string.execute_user_portfolio_activities, roles)) }

            activityCardSection(
                itemList = portfolioActivityList,
                onItemSelected = viewModel::updateActivityCardSelected,
                isItemSelected = {
                    viewModel.cardStateChange(itemSelected = selectedActivityCard, item = it)
                }
            )

            activityCardSection(
                itemList = portfolioList,
                onItemSelected = viewModel::updateActivityCardSelected,
                isItemSelected = {
                    viewModel.cardStateChange(itemSelected = selectedActivityCard, item = it)
                }
            )

            item { ActivityCardTitle(title = stringResource(R.string.execute_user_activities, roles)) }

            activityCardSection(
                itemList = activityList,
                onItemSelected = viewModel::updateActivityCardSelected,
                isItemSelected = {
                    viewModel.cardStateChange(itemSelected = selectedActivityCard, item = it)
                }
            )

            activityCardSection(
                itemList = historyList,
                onItemSelected = viewModel::updateActivityCardSelected,
                isItemSelected = {
                    viewModel.cardStateChange(itemSelected = selectedActivityCard, item = it)
                },
                shouldDividerShow = false
            )
        }

    }
}
