package com.farmbase.app.ui.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import com.farmbase.app.R

/**
 * A composable function that displays a text with a specific clickable portion and style
 *
 * @param text the main text that will be displayed.
 * @param clickableText the specific part of the text that should be clickable.
 * @param onTextClick callback function triggered when the clickable text is clicked.
 */
@Composable
fun ClickableText(
    text: String,
    clickableText: String,
    onTextClick: () -> Unit,
) {
    // build an AnnotatedString to mix normal and clickable text
    val annotatedString = buildAnnotatedString {
        append(text) // append the main text

        // append the clickable text with a specific style and click interaction
        withLink(
            link = LinkAnnotation.Clickable(
                tag = clickableText,
                linkInteractionListener = { onTextClick() }, // trigger click callback when tapped
                styles = TextLinkStyles(
                    style = SpanStyle(color = colorResource(R.color.light_blue)) // set clickable text color
                )
            )
        ) {
            append(clickableText) // append the clickable text separately
        }
    }

    // display the final text with annotated styling
    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium
    )
}
