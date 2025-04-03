package com.farmbase.app.ui.widgets


import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.utils.ActivityCardItem

/**
 * Adds a section of activity cards to a LazyColumn.
 * Each card displays an icon, header, description, and selection state.
 * Optionally, a divider can be added after the list.
 *
 * @param itemList list of activity card items to be displayed.
 * @param isItemSelected function to determine if an item is selected.
 * @param onItemSelected callback invoked when an item is selected.
 * @param shouldDividerShow determines if a horizontal divider should be shown after the list.
 */
fun LazyListScope.activityCardSection(
    itemList: List<ActivityCardItem>,
    isItemSelected: (ActivityCardItem) -> Boolean,
    onItemSelected: (ActivityCardItem) -> Unit,
    shouldDividerShow: Boolean = true
) {
    // add each activity card item to the LazyColumn
    items(
        items = itemList,
        key = { it.id }
    ) { item ->
        ActivityCard(
            iconFile = item.iconFile,
            iconUrl = item.iconUrl,
            icon = item.icon,
            headerText = item.headerText,
            descriptionText = item.descText,
            isSelected = isItemSelected(item),
            onClick = { onItemSelected(item) }
        )
    }

    // optionally add a horizontal divider after the items
    if (shouldDividerShow) {
        item { HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.gray)) }
    }

}

/**
 * Displays a styled title text for an activity card section.
 * @param title The text to be displayed as the section title.
 */
@Composable
fun ActivityCardTitle(title: String){
    Text(text = title, style = MaterialTheme.typography.labelLarge)
}
