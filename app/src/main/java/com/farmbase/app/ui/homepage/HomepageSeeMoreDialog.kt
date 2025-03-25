package com.farmbase.app.ui.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun HomepageSeeMoreDialog(
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier.width(350.dp)
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(24.dp)) {
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "Cancel",
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "The homepage is designed to enable you to easily view the status of, execute an activity on or log/solve a red flag for any person, group or place within your area of responsibility." +
                                "At the top of the home page you have the header card that has the details of the person or in the case of a homepage for a group or place the person responsible for that group or place, for example a member is responsible for a farm or a Trust Group leaders is responsible for a Trust Group." +
                                "In the header card you have the following information:\n1.Program Type: This describes the type of program, for example maize program.\n2. Personal information: Basic information on person including ID number, name and role.\n3. Size of Operations: This includes the size of the operations that the person is responsible for, for example 2 Hectares.\n4. Red Flag Alert: This is the number of Red flags directly against homepage person, group or place.  For example if you are on a homepage for a specific member, you will have only the count of redflags against that member, such as Temporarily Absent member, and it would not include redflags on places they are responsible for like their farm, such as Pest and Disease.\n5. Member Information: This black circle when clicked will take you to page with all personal information on person.\n6. Log Red Flags: This red triangle when clicked will allow you to log a redflag against this specific person, group or place whose home page you are on.  If you are on your own home page, it will allow you to log a redflag against any person, group or place you are responsible for.\n7. View Activity Schedule or Select Collection Center: If you are the homepage for a place, for example a farm, clicking this button will help you select where you are to drop off your harvest, if applicable. If you are on the homepage of a person or group, this button will take you to an activity schedule that allows you to see activities to complete and redflags to solve in priority order for all people, groups or places, that the person is responsible to complete.\n8. Performance Summary: This section summarizes the performance of the person, group or place. In the grey boxes you have the number of redflags directly against them as well as the number of redflags against people, groups or places they are responsible for. In the circles with the percentage, you have their relative performance to all other individuals at the same level within the hub or organization. For example if someone has an 80% in the circle, they are performing better than 80% of their peers in the hub." +
                                "The next section of the homepage contains the details of the person, group or place that the homepage owner is  responsible for. It has the activities, red flags and performance of the person, group or place.\n1. Status and their respective colours: The status of an activity or a red flag can be grouped into due, late and very late. This colour is displayed on the shape positioned at the far right of the cards. Yellow represents \"Due\", orange represents \"late\", and red represents \"very late\".\n2. Execute Activities Section: In this section that provides details of the person, group or place that the homepage owner is  responsible for, the execute activity section contains activities yet to be completed that the homepage owner is supposed to do to the person, group or place they are responsible for. For example, on the field officer homepage, you will see activities to be done on trust groups, members etc. These activities to be completed are grouped by the different persons, groups or place that the homepage owner is responsible for. For example, the activities for farms are grouped together, activities for members are grouped together etc. For activities yet to be completed in a group, the number of activities will be displayed in a circle on the group card. The colour of the circle will represent the most critical status of the activity. Example, if there are 7 activities in a group, with four activities due, two activities late, and one activity very late, the colour of the circle will be the colour of the most critical activity status which is \"very late\".\n3. Solve Red Flags Section: In this section that provides details of the person, group or place that the homepage owner is  responsible for, the solve red flag section contains red flags that are not solved logged against the people, place or groups the homepage owner is responsible for. For example, on the field officer homepage, you will see redflags logged against trust groups, members etc. These redflags are grouped by the different persons, groups or place that the homepage owner is responsible for. For example, the redflags for farms are grouped together, redflags for members are grouped together etc. For red flags yet to be solved in a group, the number of redflags will be displayed in a circle on the group card. The colour of the circle will represent the most critical status of the red flag. Example, if there are 7 red flags in a group, with four red flags due, two red flags late, and one red flag very late, the colour of the circle will be the colour of the most critical red flag status which is \"very late\".\n4. Performance Section: This section lists and displays the performance of the place, group or person, one level below the homepage owner. For example, the list of farms will be displayed on the performance section of the member\'s homepage, the list of members will be displayed on the performance section of the trust group\'s homepage etc. In the grey boxes you have the number of redflags directly against them.  In the circles with the percentage, you have their relative performance to other places, groups or persons at the same level within the hub or organization.  For example if someone has an 80% in the circle, they are performing better than 80% of their peers in the hub or organization." +
                                "The next section of the homepage contains the details of the homepage owner. It has the activities, red flags, performance dashboard and history of the homepage owner.\n1. Execute Activities Section: In this section that provides details of the homepage owner, the execute activity section contains activities yet to be completed on the homepage owner. For example, on the farm homepage, you can see fertilizer 1 activity to be done on the farm, on the member homepage, you will see share knowledge activity to be done on the member etc.  The status of the activity will be displayed by the colour of the rectangle on the card. The rectangle also displays the number of days the activity is late by. For example, if the share knowledge activity is late by three days, \"3\" will be displayed on the rectangle with the respective colour for late.\n2. Solve Red Flags Section: In this section that provides details of the homepage owner, the solve red flag section contains red flags logged against the homepage owner that are yet to be solved. For example, on the farm homepage, you can see weed red flag to be solved on the farm, on the member homepage, you can see \"temporary absence\" red flag logged against the field officer that are yet to be solved etc. The status of the red flag will be displayed by the colour of the rectangle on the card. The rectangle also displays the number of days the red flag is late by. For example, if the weed red flag is late by three days, \"3\" will be displayed on the rectangle with the respective colour for late.\n3. Dashboard: This card when clicked will allow you to view the performance dashboard of the homepage owner.\n4. History: This card when clicked will allow you to view the activities performed on the homepage owner, red flags logged against the homepage owner, red flags solved for the homepage owner.",
                        style = MaterialTheme.typography.bodyMedium

                    )
                }
            }
        }
    }
}