package com.farmbase.app.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.farmbase.app.MainActivity.Companion.i18n4kConfig
import de.comahe.i18n4k.Locale
import x.y.MyMessages

@Composable
fun LanguageDialog(
    onDismissRequest: () -> Unit,
) {

    fun onLocaleClick(locale: Locale){
        i18n4kConfig.value = i18n4kConfig.value.withLocale(locale)
        onDismissRequest()
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    max = 400.dp
                )
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(MyMessages.setLanguage(), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                for (locale in MyMessages.locales) {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        RadioButton(
                            selected = i18n4kConfig.value.locale == locale,
                            onClick = {
                                onLocaleClick(locale)
                            })
                        Text(
                            AnnotatedString(locale.displayLanguage),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                   onLocaleClick(locale)
                                },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDialogPreview(){
    LanguageDialog {  }
}