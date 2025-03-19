package com.farmbase.app.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.farmbase.app.R
import kotlinx.coroutines.Dispatchers

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
    Card(shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color =if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(id = R.color.gray)),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) colorResource(R.color.selected_card_color) else colorResource(id = R.color.white),
            contentColor = if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(id = R.color.black_text)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // ensures text is centered relative to image
        ) {
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
                contentScale = ContentScale.Crop
            )

            // spacer for separation
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f), // takes remaining space
                verticalArrangement = Arrangement.Center // centers text in the available space
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) colorResource(id = R.color.selected_card_content_color) else colorResource(
                        id = R.color.black_text
                    )
                )

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
    }
}