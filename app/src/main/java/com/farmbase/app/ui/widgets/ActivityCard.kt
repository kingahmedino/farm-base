package com.farmbase.app.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.farmbase.app.R
import kotlinx.coroutines.Dispatchers

/**
 * A composable function that displays an activity card with an image,
 * header text, description, selection state, and a count in the top-right corner.
 *
 * @param iconUrl  URL used to fetch the image to be displayed
 * @param icon resource ID for a placeholder or fallback image
 * @param headerText title text of displaying the activity label
 * @param descriptionText description text below the header
 * @param isSelected boolean indicating if the card is selected, affecting its styling.
 * @param count optional count text displayed at the top-right if greater than 0.
 * @param onClick lambda function executed when the card is clicked.
 */
@Composable
fun ActivityCard(
    iconUrl: String? = null,
    icon: Int,
    headerText: String,
    descriptionText: String? = null,
    isSelected :Boolean = false,
    count: Int = 0,
    onClick: () -> Unit,
) {
    // card container with rounded corners and border color based on selection state
    Card(shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color =if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(id = R.color.gray)),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) colorResource(R.color.selected_card_color) else colorResource(id = R.color.white),
            contentColor = if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(id = R.color.black_text)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // makes the entire card clickable
    ) {
        // box ensures elements can overlap, allowing the count badge to be positioned correctly
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically // ensures text is centered relative to image
            ) {
                // loads an image from URL or fallback resource
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(iconUrl ?: icon)
                        .placeholder(icon) // placeholder image while loading
                        .error(icon) // error image if loading fails
                        .dispatcher(Dispatchers.IO) // not sure if this is needed need to test this
                        .build(),
                    contentDescription = "Card Image",
                    modifier = Modifier
                        .height(72.dp)
                        .width(72.dp),
                    contentScale = ContentScale.FillBounds
                )

                // spacer for separation
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f).padding(end = if (count>0) 25.dp else 0.dp), // takes remaining space
                    verticalArrangement = Arrangement.Center // centers text in the available space
                ) {
                    // header text with dynamic color based on selection state
                    Text(
                        text = headerText,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(
                            id = R.color.black_text
                        )
                    )

                    // conditionally display description text if available
                    descriptionText?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(
                                id = R.color.black_text
                            )
                        )
                    }
                }
            }

            // displays the count card only if `count` is greater than 0
            if (count > 0) {
               CountCard(modifier = Modifier
                   .align(Alignment.TopEnd)
                   .padding(top = 12.dp, end = 12.dp),
                   count = count)
            }
        }
    }
}

/**
 * A composable function that displays a circular count badge.
 * @param modifier modifier to allow customization of layout behavior.
 * @param count numerical value to be displayed inside the card.
 */
@Composable
fun CountCard(
    modifier: Modifier,
    count: Int
){
    // create a circular card to display the count value
    Card(
        shape = CircleShape,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.red))
    ) {
        // center the text inside the card
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(30.dp),
        ) {
            Text(
                text = count.toString(),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}