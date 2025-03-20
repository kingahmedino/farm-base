package com.farmbase.app.ui.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.CountCard

@Composable
fun UserData(
    image: Int,
    userName: String,
    userId: String,
    userRole: String,
    hectareSize: Double = 0.0,
    rfCount: Int = 0
) {
    Card(
        shape = RectangleShape,
        border = BorderStroke(width = 1.dp, color = colorResource(R.color.gray)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
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
                        .data(image)
                        .placeholder(image) // placeholder image while loading
                        .error(image) // error image if loading fails
                        .build(),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .height(72.dp)
                        .width(72.dp),
                    contentScale = ContentScale.Crop
                )

                // spacer for separation
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f).padding(end = if (rfCount>0) 25.dp else 0.dp), // takes remaining space
                    verticalArrangement = Arrangement.Center // centers text in the available space
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userId,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userRole,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .background(shape = RoundedCornerShape(4.dp), color = colorResource(R.color.light_gray))
                                    .padding(10.dp),
                            text = hectareSize.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Hectare",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                }
            }

            // displays the count card only if `count` is greater than 0
            if (rfCount > 0) {
                CountCard(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp),
                    count = rfCount)
            }
        }
    }
}