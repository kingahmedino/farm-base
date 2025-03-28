package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.ui.widgets.ActivityCardTitle
import com.farmbase.app.ui.widgets.activityCardSection
import com.farmbase.app.utils.ActivityCardItem

@Composable
fun HomepageDetailScreen(
    role: String,
    paddingValues: PaddingValues,
    sections: List<Pair<String, List<List<ActivityCardItem>>>>,
    viewModel: HomepageViewModel
) {
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { HomepageHeader(role, showDialog, onDialogDismiss = { showDialog = false }) }

        sections.forEach { (title, lists) ->
            item { ActivityCardTitle(title = title) }

            lists.forEachIndexed { listIndex, activityItemList ->
                activityCardSection(
                    itemList = activityItemList,
                    onItemSelected = viewModel::updateActivityCardSelected,
                    isItemSelected = { viewModel.cardStateChange(selectedActivityCard, it) },
                    shouldDividerShow = listIndex != lists.lastIndex
                )
            }
        }
    }
}

