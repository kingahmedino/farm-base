package com.farmbase.app.ui.formBuilder.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A circular rating component that allows users to select a numerical rating.
 *
 * This composable displays a row of numbered circles that can be clicked to set a rating.
 * It also shows "Very Poor" and "Very Good" labels at the extremes when multiple rating
 * options are available.
 *
 * @param modifier Modifier to be applied to the composable
 * @param rating Current rating value
 * @param maxRating Maximum possible rating value, determines number of circles shown
 * @param shapeColor Color used for selected rating circles
 * @param unselectedColor Color used for unselected rating circles
 * @param onRatingChanged Callback that provides the new rating value when user selects a rating
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Rating(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    maxRating: Int = 5,
    lowRatingText: String,
    highRatingText: String,
    shapeColor: Color = Color.Yellow,
    unselectedColor: Color = Color.Gray.copy(alpha = 0.3f),
    onRatingChanged: (Double) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (index in 1..maxRating) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .background(
                            color = if (index <= rating) shapeColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = if (index <= rating) shapeColor else unselectedColor,
                            shape = CircleShape
                        )
                        .clickable { onRatingChanged(index.toDouble()) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = index.toString(),
                        color = if (index <= rating) Color.Black else unselectedColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Labels for first and last ratings
        if (maxRating > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = lowRatingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    text = highRatingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}