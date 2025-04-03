package com.farmbase.app.sync

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.farmbase.app.R
import com.farmbase.app.repositories.IconsRepository
import com.farmbase.app.utils.Constants
import java.io.File
import kotlin.random.Random

class IconDownloadWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val iconsRepository: IconsRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Create notification channel
        // createNotificationChannel(applicationContext)

        // Start foreground service
        // setForeground(createForegroundInfo("Starting icon downloads..."))

        return try {
            val icons = iconsRepository.getAllIconsToDownload()
            // Process icons in batches (e.g., 20 at a time)
            val batchSize = 20  // You can adjust the batch size as needed
            val iconBatches = icons.chunked(batchSize)

            iconBatches.forEach { batch ->
                // Update notification progress
                // setForeground(createForegroundInfo("Downloading batch ${batchIndex + 1} of ${iconBatches.size}"))

                batch.forEachIndexed { index, icon ->
                    // Update notification progress for individual icon
                    // setForeground(createForegroundInfo("Downloading icon ${index + 1} of ${batch.size}"))

                    // Download the image
                    val downloadSuccess = downloadImage(applicationContext, icon.iconId, icon.iconUrl)
                    val status = if (downloadSuccess) Constants.IconsDownloadStatus.SUCCESSFUL else Constants.IconsDownloadStatus.FAILED
                    // Update the status in the database
                    iconsRepository.updateIconStatus(icon.iconId, status)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("Icon download failed: ${e.message}", "failed")
            Result.failure()
        }
    }

    private fun createForegroundInfo(progressText: String): ForegroundInfo {
       /* val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "icon_download_channel")
            .setContentTitle("Icon Download")
            .setContentText(progressText)
            .setSmallIcon(R.drawable.ic_history)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOngoing(true)  // Notification can't be dismissed
            .build()*/

        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(applicationContext, "icon_download_channel")
                .setSmallIcon(R.drawable.ic_history)
                .setContentText(progressText)
                .setContentTitle("Icon Download")
                .setOngoing(true)
                .build()
        )
    }

    private fun downloadImage(context: Context, filename: String, downloadUrl: String): Boolean {
        val mimeType = if (downloadUrl.endsWith(".jpg", true)) {
            "image/jpeg"
        } else {
            "image/png"
        }

        try {
            // get the destination directory (Pictures directory)
            // val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val picturesDirectory = File(context.getExternalFilesDir( Environment.DIRECTORY_PICTURES), "Icons")
            val iconFile = File(picturesDirectory, "$filename.jpg")

            // check if the directory exists, and create it if it doesn't
            if (!picturesDirectory.exists()) {
                picturesDirectory.mkdirs() // create the directory if it doesn't exist
            }

            // check if the file already exists
            if (iconFile.exists()) {
                Log.i("IconDownload", "File already exists: $filename.jpg")
                return true // return true, as the icon is already downloaded
            }

            // if the file doesn't exist, proceed with the download
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(downloadUrl)
            val request = DownloadManager.Request(downloadUri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setAllowedOverRoaming(false)
                setTitle(filename)
                setMimeType(mimeType)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationUri(Uri.fromFile(iconFile)) // use the file path in the directory provided
            }
            dm.enqueue(request)
            Log.i("Success", "Image download started for $filename")
            return true
        } catch (e: Exception) {
            Log.e("Failed", "Image download failed for $filename: ${e.message}")
            return false
        }
    }



    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "icon_download_channel",
                "Icon Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification channel for icon downloads"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
