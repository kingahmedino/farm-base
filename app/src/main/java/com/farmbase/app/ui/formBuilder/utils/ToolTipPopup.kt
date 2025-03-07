package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

/**
 * A composable that displays a tooltip icon with explanatory text on click.
 *
 * This component shows a small information icon that, when clicked, displays a tooltip
 * containing the provided text. It's useful for providing additional context or help
 * information for form fields or other UI elements.
 *
 * @param toolTipText The text to display in the tooltip when the icon is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipIcon(toolTipText: String) {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(

        ),
        tooltip = {
            RichTooltip {
                Text(text = toolTipText)
            }
        },
        state = tooltipState
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Help,
            contentDescription = "tooltip",
            modifier = Modifier.clickable {
                scope.launch {
                    tooltipState.show()
                }
            }
        )
    }
}