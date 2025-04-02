package com.farmbase.app.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    showBottomSheet: Boolean,
    sheetColor: Color,
    headerText: String,
    descText: String,
    textColor: Color,
    buttonColor: Int,
    buttonTextColor: Int,
    iconTint: Int,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit ) {

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest ,
            sheetState = sheetState,
            shape = RectangleShape,
            containerColor = sheetColor,
            tonalElevation = 16.dp,
            dragHandle = {
                Box(modifier = Modifier.background(Color.Transparent))
            }
        ) {
            BottomSheetItem(
                headerText = headerText,
                descText = descText,
                textColor = textColor,
                buttonColor = buttonColor,
                buttonTextColor = buttonTextColor,
                iconTint = iconTint,
                onButtonClick = onButtonClick
            )
        }
    }
}

@Composable
fun BottomSheetItem(headerText: String,
                    descText: String,
                    textColor: Color,
                    buttonColor: Int,
                    buttonTextColor: Int,
                    iconTint: Int,
                    onButtonClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = headerText,
            style = MaterialTheme.typography.labelLarge,
            color = textColor)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = descText,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor)
        Spacer(modifier = Modifier.height(16.dp))

        NextButtonEnabled(
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth().height(40.dp),
            buttonColor = buttonColor,
            buttonTextColor = buttonTextColor,
            iconTint = iconTint
        )

    }
}