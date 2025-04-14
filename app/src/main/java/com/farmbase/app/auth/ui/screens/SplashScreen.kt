package com.farmbase.app.auth.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.farmbase.app.auth.util.AuthObjects.launchWebsite

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current

    SplashContent(onClick = {
        launchWebsite(context = context)
    }, snackBarHostState = snackBarHostState)

}


@Composable
@Preview
fun SplashScreenPreview() {
//    SplashScreen()
}

