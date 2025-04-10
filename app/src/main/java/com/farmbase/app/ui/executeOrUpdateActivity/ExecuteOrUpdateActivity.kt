package com.farmbase.app.ui.executeOrUpdateActivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.ui.widgets.ActivityCard
import com.farmbase.app.ui.widgets.BottomSheet
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.ui.widgets.activityCardSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecuteOrUpdateActivityScreen(
    activity: String,
    onBackButtonClicked:() -> Unit,
    onNextButtonClicked:() -> Unit,
    viewModel: ExecuteOrUpdateActivityViewModel = hiltViewModel()
) {
    val list by viewModel.programList.collectAsStateWithLifecycle()
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()
    val activityCard by viewModel.activityCard.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

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

    LaunchedEffect(activity) {
       viewModel.updateSelectedActivityItem(activity)
    }
    Scaffold(modifier = Modifier,
      topBar = { TopBar(modifier = Modifier.fillMaxWidth()) {onBackButtonClicked()} },
        bottomBar = {
            NextButton(
                onClick = { if(selectedActivityCard?.headerText == context.getString(R.string.complete_activity)) {onNextButtonClicked()} else { showBottomSheet = false }},
                enabled = selectedActivityCard != null,
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
            Spacer(modifier = Modifier.height(20.dp))

            ActivityCard(
                iconUrl = activityCard?.iconUrl,
                iconFile = null,
                icon = activityCard?.icon?: R.drawable.ic_alert,
                headerText = activityCard?.headerText ?:"",
                isSelected = false,
                onClick = {},
                radius = 0.dp,
                )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.gray))
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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