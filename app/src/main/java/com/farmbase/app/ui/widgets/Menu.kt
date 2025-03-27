package com.farmbase.app.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R

@Composable
fun Menu(
    expanded: Boolean,
    menuItem: List<Pair<String, ImageVector>>,
    onDismissRequest: () -> Unit,
    onClick: (String) -> Unit,
) {
    DropdownMenu(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        menuItem.forEach { (title, leadingIcon) ->
            DropdownMenuItem(
                text = { Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.gray))
                       },
                leadingIcon = {
                    Icon(imageVector = leadingIcon,
                    contentDescription = title,
                    tint = colorResource(R.color.gray) )
                              },
                onClick = { onClick(title) }
            )

        }
    }
}