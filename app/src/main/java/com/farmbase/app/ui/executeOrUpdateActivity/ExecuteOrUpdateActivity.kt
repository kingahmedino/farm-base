package com.farmbase.app.ui.executeOrUpdateActivity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.ui.widgets.ActivityCard
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.ui.widgets.activityCardSection

@Composable
fun ExecuteOrUpdateActivityScreen(
    activity: String,
    onBackButtonClicked:() -> Unit,
    viewModel: ExecuteOrUpdateActivityViewModel = hiltViewModel()
) {
    val list by viewModel.programList.collectAsStateWithLifecycle()
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()
    val activityCard by viewModel.activityCard.collectAsStateWithLifecycle()

    LaunchedEffect(activity) {
       viewModel.updateSelectedActivityItem(activity)
    }
    Scaffold(modifier = Modifier,
      topBar = { TopBar(modifier = Modifier.fillMaxWidth()) {onBackButtonClicked()} },
        bottomBar = {
            NextButton(
                onClick = { },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)) {

            DoubleText(
                modifier = Modifier,
                mainText = R.string.execute_or_update_activity,
                subText = R.string.execute_or_update_activity_desc
            )
            Spacer(modifier = Modifier.height(16.dp))

            ActivityCard(
                iconUrl = activityCard?.iconUrl,
                iconFile = activityCard?.iconFile,
                icon = R.drawable.ic_alert,
                headerText = activityCard?.headerText?:"",
                isSelected = false,
                onClick = {},
                radius = 0.dp,
                )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.gray))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                activityCardSection(
                    itemList = list,
                    isItemSelected = { selectedActivityCard == it },
                    onItemSelected = viewModel::updateSelectedCard,
                    shouldDividerShow = false
                )
            }
        }
    }
}