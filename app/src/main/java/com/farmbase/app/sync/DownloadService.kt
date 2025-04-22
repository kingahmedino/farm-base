package com.farmbase.app.sync

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class DownloadService : Service() {

    private lateinit var downloadManager: DownloadManager
    private val channelId = "download_channel"
    private val notificationId = 1
    private val cancelAction = "cancel_download_action"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val urls = intent?.getStringArrayListExtra("urls") ?: return START_NOT_STICKY
        if (urls.isEmpty()) return START_NOT_STICKY

        createNotificationChannel()
        startForeground(notificationId, buildNotification("Download started..."))

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        CoroutineScope(Dispatchers.IO).launch {
            downloadInBatches(urls)
            updateNotification("All downloads complete!")
            delay(2000)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private suspend fun downloadInBatches(urls: List<String>, batchSize: Int = 15) {

        val iconDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Icons").apply {
            if (!exists()) mkdirs()
        }

        urls.chunked(batchSize).forEachIndexed { batchIndex, batch ->
            Log.d("DownloadService", "Starting batch ${batchIndex + 1}")

            batch.forEach { url ->
                val fileName = "${url.substringAfterLast("/").substringBeforeLast(".")}.jpg"
                val iconFile = File(iconDir, fileName)

                if (iconFile.exists()) {
                    Log.d("DownloadService", "Skipping: $fileName (already exists)")
                    updateNotification("Skipping: $fileName (already exists)")
                    return@forEach
                }

                val request = buildDownloadRequest(url, iconFile)
                val downloadId = downloadManager.enqueue(request)

                monitorDownload(downloadId, fileName)
                delay(200) // To avoid rate limiting
            }

            Log.d("DownloadService", "Batch ${batchIndex + 1} complete")
        }
    }

    private fun buildDownloadRequest(url: String, destinationFile: File): DownloadManager.Request {
        val mimeType = if (url.endsWith(".jpg", true)) "image/jpeg" else "image/png"

        return DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setAllowedOverRoaming(false)
            setTitle(destinationFile.name)
            setDescription("Downloading...")
            setMimeType(mimeType)
            setDestinationUri(Uri.fromFile(destinationFile))
        }
    }

    @SuppressLint("Range")
    private suspend fun monitorDownload(downloadId: Long, fileName: String) {
        var downloading = true
        while (downloading) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor != null && cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val message = statusMessage(status)
                updateNotification("$fileName: $message")

                if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                    downloading = false
                }
            }
            cursor?.close()
            delay(500)
        }
    }

    private fun buildNotification(content: String): Notification {
        val cancelIntent = Intent(this, DownloadService::class.java).apply {
            action = cancelAction
        }

        val pendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Image Download")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancel", pendingIntent)
            .build()
    }

    private fun updateNotification(content: String) {
        val notification = buildNotification(content)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    private fun statusMessage(status: Int): String {
        return when (status) {
            DownloadManager.STATUS_FAILED -> "Download failed"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Completed"
            else -> "Unknown status"
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Download Status", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
