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
            "https://storage.googleapis.com/babbangona-prod-bucket-2/asset_manager/images/asset/contract_agreement_video_AST-0613230933_kwakwsk_kwakwsk_IK00000000_2025-03-21_1655043729451933294141680.mp4",
            "https://picsum.photos/id/8/5000/3333.jpg",
            "https://storage.googleapis.com/babbangona-prod-bucket-2/asset_manager/images/assigned_asset/assigned_asset_video_AST-0613230933_mobilListe10018_mobilListe10018_IK00000000_2025-04-09_1509548981546450491100855.mp4",
            "https://storage.googleapis.com/agric-os-media-files/activity_planning/English_Audio/Activity/Log%20Redflag.mp3",
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
            Text("Download Media")
        }
    }
}
