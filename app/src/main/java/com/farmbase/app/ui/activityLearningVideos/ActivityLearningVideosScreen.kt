package com.farmbase.app.ui.activityLearningVideos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.auth.ui.components.DoubleText
import com.farmbase.app.ui.widgets.ActivityCard
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar

@Composable
fun ActivityLearningVideosScreen(
    onBackButtonClicked:() -> Unit,
    onNextButtonClicked:() -> Unit
) {
    var isSelected by rememberSaveable {  mutableStateOf(false) }

    Scaffold(modifier = Modifier,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth()) {onBackButtonClicked()} },
        bottomBar = {
            NextButton(
                onClick = onNextButtonClicked,
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            DoubleText(
                modifier = Modifier,
                mainText = R.string.execute_or_update_activity,
                subText = R.string.execute_or_update_activity_desc
            )
            Spacer(modifier = Modifier.height(16.dp))

            ActivityCard(
                icon = R.drawable.ic_learning_videos,
                headerText = stringResource(R.string.learning_videos),
                descriptionText = stringResource(R.string.learning_videos_desc),
                isSelected = isSelected,
                onClick = { isSelected = !isSelected},
                radius = 0.dp,
            )
        }
    }
}