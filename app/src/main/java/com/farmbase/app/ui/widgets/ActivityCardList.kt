package com.farmbase.app.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ActivityItem(
    val id: Int = 0,
    val icon: Int,
    val headerText: String,
    val descText: String? = null,
    val isSelected: Boolean = false,
    val count: Int = 0,
)

@Composable
fun ActivityCardList(activityList: List<ActivityItem>, onCardClicked: (Pair<Int, String>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            key = { it.id },
            items = activityList) { item ->
            ActivityCard(
                icon = item.icon,
                headerText = item.headerText,
                descriptionText = item.descText,
                isSelected = item.isSelected,
                onClick = {
                    onCardClicked(Pair(item.id, item.headerText))
                }
            )
        }
    }

//        activityList.forEachIndexed { index, item ->
//            ActivityCard(
//                icon = item.icon,
//                headerText = item.headerText,
//                descriptionText = item.descText,
//                isSelected = item.isSelected,
//                count = item.count,
//                onClick = {
//                    onCardClicked(Pair(index, item.headerText))
//                }
//            )
//        }
    }
}