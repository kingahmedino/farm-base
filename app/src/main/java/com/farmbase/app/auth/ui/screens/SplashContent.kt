package com.farmbase.app.auth.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmbase.app.R
import com.farmbase.app.auth.globalsnackbar.SnackBarViewModel
import com.farmbase.app.auth.internetconnectionobserver.ConnectivityViewModel
import com.farmbase.app.auth.util.AuthObjects.launchWebsite
import com.farmbase.app.auth.util.convertRawStringToString
import com.farmbase.app.ui.theme.FredokaFontFamily
import com.farmbase.app.ui.widgets.NextButtonEnabled
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    snackBarHostState: SnackbarHostState
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                colorResource(R.color.cafitech_dark_green)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(R.drawable.group_4), contentDescription = "null")

        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(24.dp)
        )

        Text(
            stringResource(R.string.cafitech),
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FredokaFontFamily
        )
        Text(
            stringResource(R.string.ecosystem),
            fontSize = 32.sp,
            color = Color.White,
            modifier = modifier.padding(top = 12.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = FredokaFontFamily
        )

        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
        )

//        ButtonWithInternetCheck(
//            onClick = onClick,
//            text = "Login",
//            snackBarHostState = snackBarHostState
//        )

              NextButtonEnabled(
                  modifier = modifier
                      .fillMaxWidth()
                      .padding(horizontal = 24.dp),
                  buttonColor = R.color.cafitech_light_green,
                  onClick = {

//                      if (!isConnected) {
//                          coroutineScope.launch {
//                              snackBarViewModel.showSnackbar()
//                          }
//                      } else {
//                          // do the thing
//                      }


                      // do nothing
                      launchWebsite(context = context)
                  },
                  buttonText = stringResource(R.string.login),
                  shouldIconShow = false
              )


    }

}

@Composable
fun ButtonWithInternetCheck(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    connectivityViewModel: ConnectivityViewModel = hiltViewModel(),
    snackBarViewModel: SnackBarViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    val messageText = convertRawStringToString(R.string.internet_connection_unavailable)
    val buttonText = convertRawStringToString(R.string.okay)

    Button(
        onClick = {
            if (connectivityViewModel.isConnected.value) {
                onClick()
            } else {
                coroutineScope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()

                    snackBarViewModel.showAppSnackBar(
                        message = messageText,
                        buttonMessage = buttonText
                    )
                }
            }
        },
        modifier = modifier
    ) {
        Text(text)
    }
}


@Composable
@Preview
fun SplashContentPreview() {
//    SplashContent()
}