package com.farmbase.app.ui.selectHomepage

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
import com.farmbase.app.ui.widgets.ClickableText
import com.farmbase.app.ui.widgets.NextButton
import com.farmbase.app.ui.widgets.TopBar
import com.farmbase.app.ui.widgets.activityCardSection

@Composable
fun SelectHomepageScreen(
    onBackButtonClicked: ()-> Unit,
    onNextButtonClicked: ()-> Unit,
    viewModel: SelectHomepageViewModel = hiltViewModel()
) {
    val entitiesList by viewModel.combinedList.collectAsStateWithLifecycle()
    val selectedActivityCard by viewModel.selectedActivityCard.collectAsStateWithLifecycle()


    Scaffold(modifier = Modifier,
        topBar = {
            TopBar(modifier = Modifier.fillMaxWidth()){ onBackButtonClicked() }
        },
        bottomBar = { NextButton(
            onClick = { onNextButtonClicked() },
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
                    text = stringResource(R.string.select_homepage),
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                ClickableText(
                    text = stringResource(R.string.select_homepage_desc_less),
                    clickableText = stringResource(R.string.see_more)) { }
            }

            activityCardSection(
                itemList = entitiesList,
                isItemSelected = { selectedActivityCard == it },
                onItemSelected = viewModel::updateSelectedCard,
                shouldDividerShow = false
            )

        }

    }
}