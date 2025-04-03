package com.farmbase.app.auth.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmbase.app.R
import com.farmbase.app.auth.util.AuthObjects.launchWebsite
import com.farmbase.app.ui.theme.FredokaFontFamily
import com.farmbase.app.ui.widgets.NextButtonEnabled

@Composable
fun SplashScreen(modifier: Modifier = Modifier, innerPadding: PaddingValues) {
    val context = LocalContext.current

//    Scaffold (
//        content = {
         //   paddingValues ->

            Column(modifier = modifier
                //.padding(paddingValues)
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                colorResource(R.color.cafitech_dark_green)
            ), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Image(painter = painterResource(R.drawable.group_4), contentDescription = "null")

                Spacer(modifier = modifier.fillMaxWidth().height(24.dp))

                Text(stringResource(R.string.cafitech), fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FredokaFontFamily)
                Text(stringResource(R.string.ecosystem), fontSize = 32.sp, color = Color.White, modifier = modifier.padding(top = 12.dp), fontWeight = FontWeight.Bold, fontFamily = FredokaFontFamily)

                Spacer(modifier = modifier.fillMaxWidth().height(48.dp))

                NextButtonEnabled(
                    modifier = modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    buttonColor = R.color.cafitech_light_green,
                    onClick = {
                        // do nothing
                        launchWebsite(context = context)
                    },
                    buttonText = stringResource(R.string.login),
                    shouldIconShow = false
                )
            }

//        }
//    )


}



@Composable
@Preview
fun SplashScreenPreview() {
//   SplashScreen()
}

