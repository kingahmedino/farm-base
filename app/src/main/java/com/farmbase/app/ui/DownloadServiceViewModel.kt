package com.farmbase.app.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.farmbase.app.sync.DownloadService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DownloadServiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
):ViewModel() {
    fun startDownload(urls: List<String>) {
        val intent = Intent(context, DownloadService::class.java).apply {
            putStringArrayListExtra("urls", ArrayList(urls))
        }
        ContextCompat.startForegroundService(context, intent)
    }
}