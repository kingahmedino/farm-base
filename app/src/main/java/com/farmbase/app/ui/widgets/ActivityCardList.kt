package com.farmbase.app.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmbase.app.R

data class ActivityItem(
    val icon: Int,
    val headerText: String,
    val descText: String? = null,
    val isSelected: Boolean = false
)

@Composable
fun ActivityCardList() {
    // Sample data: A list of activity items with initial isSelected = false
    val activityList = remember {
        mutableStateListOf(
            ActivityItem(icon = R.drawable.ic_personal_info, headerText = "Activity 1"),
            ActivityItem(icon = R.drawable.ic_personal_info, headerText = "Activity 2"),
            ActivityItem(icon = R.drawable.ic_my_schedule, headerText = "Activity 3")
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        activityList.forEachIndexed { index, item ->
            ActivityCard(
                icon = item.icon,
                headerText = item.headerText,
                descriptionText = item.descText,
                isSelected = item.isSelected,
                onClick = {
                    val selectedItem = activityList[index]
                    activityList.replaceAll { item ->
                        item.copy(isSelected = if (item == selectedItem) !item.isSelected else false)
                    }
                }
            )
        }
    }
}