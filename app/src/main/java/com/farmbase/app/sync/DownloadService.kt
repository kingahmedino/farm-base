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
    private var downloadId: Long = -1L
    private val channelId = "download_channel"
    private val notificationId = 1
    private val cancelAction = "cancel_download_action"


    @SuppressLint("Range")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       /* if (intent?.action == cancelAction) {
            cancelDownload()
            return START_NOT_STICKY
        }*/
        val urls = intent?.getStringArrayListExtra("urls") ?: return START_NOT_STICKY
        if (urls.isEmpty()) return START_NOT_STICKY

        Log.d("Urls", urls.toString())


        createNotificationChannel()
        startForeground(notificationId, buildNotification("Download started..."))

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        CoroutineScope(Dispatchers.IO).launch {
           /* urls.forEach { url ->
                val mimeType = if (url.endsWith(".jpg", true)) "image/jpeg" else "image/png"

                val picturesDirectory = File(
                    applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Icons"
                )
                if (!picturesDirectory.exists()) picturesDirectory.mkdirs()

                val fileName = "${url.substringAfterLast("/").substringBeforeLast(".")}.jpg"
                val iconFile = File(picturesDirectory, fileName)

                if (iconFile.exists()) {
                    Log.d("DownloadService", "Skipping download, already exists: $fileName")
                    completedCount++
                    updateNotification("Skipped $completedCount of ${urls.size}")
                    return@forEach
                }

                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    setAllowedOverRoaming(false)
                    setTitle(fileName)
                    setDescription("Downloading file...")
                    setMimeType(mimeType)
                    setDestinationUri(Uri.fromFile(iconFile))
                }

                Log.d("DownloadService", "Downloading: $fileName")

                delay(200) // Prevent rate limit
                downloadId = downloadManager.enqueue(request)

                var downloading = true
                while (downloading) {
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor = downloadManager.query(query)
                    if (cursor != null && cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        val message = statusMessage(status)
                        updateNotification(message)

                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            downloading = false
                            completedCount++
                            updateNotification("Download of $fileName ${if (status == DownloadManager.STATUS_SUCCESSFUL) "complete" else "failed"}")
                        }
                    }
                    cursor?.close()
                    delay(500)
                }
            }*/

            val batchSize = 5
            val iconDir = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Icons")
            if (!iconDir.exists()) iconDir.mkdirs()

            urls.chunked(batchSize).forEachIndexed { batchIndex, batch ->
                Log.d("DownloadService", "Starting batch ${batchIndex + 1} of ${urls.size / batchSize + 1}")

                batch.forEach { url ->
                    val mimeType = if (url.endsWith(".jpg", true)) "image/jpeg" else "image/png"

                    val fileName = "${url.substringAfterLast("/").substringBeforeLast(".")}.jpg"
                    val iconFile = File(iconDir, fileName)

                    if (iconFile.exists()) {
                        Log.d("DownloadService", "Skipping download, already exists: $fileName")
                        updateNotification("File $fileName already exists")
                        return@forEach
                    }

                    val request = DownloadManager.Request(Uri.parse(url)).apply {
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        setAllowedOverRoaming(false)
                        setTitle(fileName)
                        setDescription("Downloading file...")
                        setMimeType(mimeType)
                        setDestinationUri(Uri.fromFile(iconFile))
                    }

                    Log.d("DownloadService", "Downloading: $fileName")

                    delay(200) // To avoid triggering rate limits
                    downloadId = downloadManager.enqueue(request)

                    var downloading = true
                    while (downloading) {
                        val query = DownloadManager.Query().setFilterById(downloadId)
                        val cursor = downloadManager.query(query)
                        if (cursor != null && cursor.moveToFirst()) {
                            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            val message = statusMessage(status)
                            updateNotification(message)

                            if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                                downloading = false
                                updateNotification("Download of $fileName ${if (status == DownloadManager.STATUS_SUCCESSFUL) "complete" else "failed"}")
                            }
                        }
                        cursor?.close()
                        delay(500)
                    }
                }

                Log.d("DownloadService", "Batch ${batchIndex + 1} complete")
            }


            updateNotification("All downloads complete!")
            delay(2000)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

            return START_NOT_STICKY
    }

    private fun cancelDownload() {
        if (downloadId != -1L) {
            downloadManager.remove(downloadId)
        }
        updateNotification("Download canceled")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(content: String): Notification {
        val cancelIntent = Intent(this, DownloadService::class.java).apply {
            action = cancelAction
        }

        val pendingCancelIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Image Download")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            //.setProgress(100, progress, !showProgress)
            //.setOngoing(showProgress)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancel", pendingCancelIntent)
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
            DownloadManager.STATUS_PAUSED -> "Download paused"
            DownloadManager.STATUS_PENDING -> "Download pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Download complete"
            else -> "Unknown status"
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Download Status", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
