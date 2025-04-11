package com.farmbase.app.i18n.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmbase.app.R
import com.farmbase.app.i18n.utils.I18nManager


@Composable
fun LanguagesMenuButton(
    isExpanded: Boolean = false,
){

    Box(
        Modifier
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(5.dp))
    ) {
       Row(
           modifier = Modifier
               .padding(vertical = 5.dp, horizontal = 10.dp),
           horizontalArrangement = Arrangement.spacedBy(5.dp),
           verticalAlignment = Alignment.CenterVertically,
       ) {
           if(I18nManager.currentLocal == null)Icon(
               painter = painterResource(R.drawable.ic_globe),
               contentDescription = "Globe Icon",
               tint = Color.White
           ) else  Box(
               Modifier
                   .size(20.dp)
                   .clip(CircleShape)
                   .background(color = Color.Gray)
           ){
               Text(
                   countryCodeToEmojiFlag(if(I18nManager.currentLocal?.language == "en") "GB"
                   else I18nManager.currentLocal!!.isO3Country),
                   modifier = Modifier
                       .fillMaxSize(),
               )
           }
           Text(
               text = if(I18nManager.currentLocal == null) "Language"
               else I18nManager.currentLocal!!.displayLanguage,
               style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
           )
           Icon(
               painter = painterResource(if(isExpanded)R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
               contentDescription = "Dropdown Icon",
               tint = Color.White
           )
       }
    }
}

@Composable
@Preview(showBackground = true)
fun LanguagesMenuButtonPreview(){
    LanguagesMenuButton()
}