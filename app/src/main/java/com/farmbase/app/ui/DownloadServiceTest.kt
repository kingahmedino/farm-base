package com.farmbase.app.ui

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TestDownloadService(viewModel: DownloadServiceViewModel = hiltViewModel()) {
    val urlList = remember {
        mutableStateListOf(
            "https://storage.googleapis.com/agricos-dev-bucket-1/string/bird_20250205113018642.png",
            "https://storage.googleapis.com/agricos-dev-bucket-1/configIcons/BGO/RF_High%20Risk(BGO).png",
            "https://www.freepik.com/free-psd/macaroon-isolated-transparent-background_137449294.htm#fromView=keyword&page=1&position=1&uuid=9b163ae4-5765-4e96-8857-1bd11e4c3ee8&query=Png",
            "https://picsum.photos/id/8/5000/3333.jpg",
            "https://picsum.photos/id/9/5000/3269.jpg"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Button(
            onClick = { viewModel.startDownload(urlList) },
        ) {
            Text("Download Image")
        }
    }
}