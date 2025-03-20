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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.CountCard


/**
 * A composable function that displays user information in a card layout.
 * It consists of `UserData` for main user details and `ExtraUserData` for additional actions.
 *
 * @param image The resource ID of the user's image.
 * @param userName The name of the user.
 * @param userId The unique identifier of the user.
 * @param userRole The role or designation of the user.
 * @param hectareSize The size of the land the user manages (default is 0.0).
 * @param rfCount The count of red flags associated with the user (default is 0).
 */
@Composable
fun UserInfoCard(
    image: Int,
    userName: String,
    userId: String,
    userRole: String,
    hectareSize: Double = 0.0,
    rfCount: Int = 0
) {
    Column {
        UserData(image, userName, userId, userRole, hectareSize, rfCount)
        Spacer(modifier = Modifier.height(16.dp))
        ExtraUserData()
    }
}


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
                        .height(90.dp)
                        .width(90.dp),
                    contentScale = ContentScale.Crop
                )

                // spacer for separation
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (rfCount > 0) 25.dp else 0.dp), // takes remaining space
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
                                .background(
                                    shape = RoundedCornerShape(4.dp),
                                    color = colorResource(R.color.light_gray)
                                )
                                .padding(10.dp),
                            text = hectareSize.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.hectare),
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

@Composable
fun ExtraUserData() {
    Card(
        shape = RectangleShape,
        border = BorderStroke(width = 1.dp, color = colorResource(R.color.gray)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

            Column(modifier = Modifier.width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_personal_info),
                    contentDescription = stringResource(R.string.personal_information),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.personal_information),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))


            Column(modifier = Modifier.width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_schedule),
                    contentDescription = stringResource(R.string.my_schedule),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.my_schedule),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}