package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.ActivityCard
import com.farmbase.app.ui.widgets.ActivityItem

fun LazyListScope.activityCardSection(
    itemList: List<ActivityItem>,
    isItemSelected: (ActivityItem) -> Boolean,
    onItemSelected: (ActivityItem) -> Unit,
    shouldDividerShow: Boolean = true
) {
    items(
        items = itemList,
        key = { it.id }
    ) { item ->
        ActivityCard(
            icon = item.icon,
            headerText = item.headerText,
            descriptionText = item.descText,
            isSelected = isItemSelected(item),
            onClick = { onItemSelected(item) }
        )
    }

    if (shouldDividerShow) {
        item { HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.gray)) }
    }

}

@Composable
fun ActivityCardTitle(title: String){
    Text(text = title, style = MaterialTheme.typography.labelLarge)
}
