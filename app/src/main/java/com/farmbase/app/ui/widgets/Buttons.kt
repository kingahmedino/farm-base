package com.farmbase.app.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.farmbase.app.R

// Buttons.kt
// This file defines reusable button components for the application.

/**
 * A composable function that creates a customizable "Next" button.
 *
 * @param onClick the function to be executed when the button is clicked.
 * @param enabled determines if the button is clickable. Defaults to true.
 * @param modifier modifier to be applied to the button.
 */
@Composable
fun NextButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier
) {
    OutlinedButton(
        modifier = modifier.navigationBarsPadding().height(dimensionResource(R.dimen.button_height)),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            // set the container color based on the enabled state
            containerColor = if (enabled) colorResource(id = R.color.green) else colorResource(id = R.color.white),
            contentColor = if (enabled) colorResource(id = R.color.green) else colorResource(id = R.color.white)
        ),
        // apply a border with a different color depending on the enabled state
        border = BorderStroke(1.dp, if (enabled) colorResource(id = R.color.green) else colorResource(id = R.color.disabled)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_standard)),
    ) {
        // display button text correct style ans color based on the buttons state
        Text(
            text =  stringResource(id = R.string.next),
            color = if (enabled) Color.White else colorResource(id = R.color.disabled),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
        // display an icon indicating "next"
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            tint = if (enabled) Color.White else colorResource(id = R.color.disabled)
        )
    }
}

/**
 * A composable function that creates a customizable "Back" button.
 *
 * @param onClick The function to be executed when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 */
@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    OutlinedButton(
        modifier = modifier.navigationBarsPadding().height(dimensionResource(R.dimen.button_height)),
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            // set a consistent yellow background for the back button
            containerColor = colorResource(id = R.color.yellow),
            contentColor = colorResource(id = R.color.black_text)
        ),
        // apply a yellow border matching the background
        border = BorderStroke(1.dp, colorResource(id = R.color.yellow)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_standard)),
    ) {
        // display back button icon
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        // display the button text
        Text(
            text =  stringResource(id = R.string.back),
            style = MaterialTheme.typography.labelLarge
        )

    }
}

@Composable
fun NextButtonEnabled(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier,
    buttonColor: Int =  R.color.green,
    buttonTextColor: Int = R.color.white,
    iconTint: Int = R.color.white
) {
    OutlinedButton(
        modifier = modifier.navigationBarsPadding().height(dimensionResource(R.dimen.button_height)),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colorResource(buttonColor),
        ),
        border = BorderStroke(1.dp, colorResource(id = buttonColor)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_standard)),
    ) {
        Text(
            text =  stringResource(id = R.string.next),
            color = colorResource(id = buttonTextColor),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
        // display an icon indicating "next"
        Icon(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            tint = colorResource(id = iconTint)
        )
    }
}