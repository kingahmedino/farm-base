package com.farmbase.app.i18n.ui

import AppStrings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.farmbase.app.i18n.utils.I18nManager
import de.comahe.i18n4k.Locale

@Composable
fun LanguagesMenu(
    isExpanded: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current

    val myLocales = AppStrings.locales

    var locales by remember { mutableStateOf(myLocales) }
    var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {

        LanguagesMenuButton(
            isExpanded = isExpanded,
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
            offset = DpOffset(x = 0.dp, y = 8.dp),
            modifier = Modifier
                .width((LocalConfiguration.current.screenWidthDp * 0.9).dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(5.dp),
                placeholder = { Text("Search") },
                value = query,
                onValueChange = { value ->
                    query = value
                    locales = myLocales.filter {
                        it.displayName.contains(
                            query,
                            ignoreCase = true
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search, contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Gray.copy(alpha = 0.15f),
                    unfocusedContainerColor = Color.Gray.copy(alpha = 0.15f),
                    focusedBorderColor = Color.Gray.copy(alpha = 0.15f),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.15f),
                    cursorColor = Color.Unspecified
                )
            )
            Spacer(Modifier.height(20.dp))
            locales.forEach { locale ->
                DropdownMenuItem(
                    leadingIcon = {
                        Box(
                            Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(color = Color.Gray)
                        ){
                            Text(
                                countryCodeToEmojiFlag(if(locale.language == "en") "GB" else locale.isO3Country),
                                modifier = Modifier
                                    .fillMaxSize(),
                            )
                        }
                    },
                    text = { Text(locale.displayLanguage) },
                    onClick = {
                        onDismiss()
                        I18nManager.setLocale(locale, context)
                    }
                )
            }
        }
    }
}

fun countryCodeToEmojiFlag(countryCode: String): String {
    return countryCode
        .uppercase(Locale.US)
        .map { char ->
            Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
        }
        .map { codePoint ->
            Character.toChars(codePoint)
        }
        .joinToString(separator = "") { charArray ->
            String(charArray)
        }
}