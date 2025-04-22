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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * Service responsible for downloading media files (images, audio, video) in batches.
 * Operates as a foreground service with notification updates on download progress.
 */
class MediaDownloadService : Service() {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "media_download_channel"
        private const val NOTIFICATION_ID = 2
        private const val BATCH_SIZE = 20
        private const val CANCEL_ACTION = "cancel_media_download_action"
        private const val NOTIFICATION_DELAY_MS = 2000L
        private const val DOWNLOAD_DELAY_MS = 200L

        // Media type maps to improve efficiency and reduce duplicate code
        /**
         * Maps canonical file extensions to all their variations
         * e.g., "jpg" -> ["jpg", "jpeg"]
         */
        private val FILE_EXTENSIONS = mapOf(
            // Images
            "jpg" to listOf("jpg", "jpeg"),
            "png" to listOf("png"),
            "svg" to listOf("svg"),
            "gif" to listOf("gif"),
            "webp" to listOf("webp"),
            "bmp" to listOf("bmp"),
            "tiff" to listOf("tiff", "tif"),
            "ico" to listOf("ico"),
            "heic" to listOf("heic"),
            "raw" to listOf("raw"),

            // Audio
            "mp3" to listOf("mp3"),
            "wav" to listOf("wav"),
            "ogg" to listOf("ogg"),
            "flac" to listOf("flac"),
            "aac" to listOf("aac"),
            "wma" to listOf("wma"),
            "m4a" to listOf("m4a"),
            "opus" to listOf("opus"),
            "midi" to listOf("mid", "midi"),

            // Video
            "mp4" to listOf("mp4"),
            "mov" to listOf("mov"),
            "avi" to listOf("avi"),
            "mkv" to listOf("mkv"),
            "webm" to listOf("webm"),
            "wmv" to listOf("wmv"),
            "flv" to listOf("flv"),
            "m4v" to listOf("m4v"),
            "3gp" to listOf("3gp"),
            "ts" to listOf("ts"),
            "mpeg" to listOf("mpeg", "mpg")
        )

        /**
         * Maps file extensions to their corresponding MIME types
         */
        private val MIME_TYPES = mapOf(
            // Images
            "jpg" to "image/jpeg",
            "png" to "image/png",
            "svg" to "image/svg+xml",
            "gif" to "image/gif",
            "webp" to "image/webp",
            "bmp" to "image/bmp",
            "tiff" to "image/tiff",
            "ico" to "image/x-icon",
            "heic" to "image/heic",
            "raw" to "image/raw",
            // Audio
            "mp3" to "audio/mpeg",
            "wav" to "audio/wav",
            "ogg" to "audio/ogg",
            "flac" to "audio/flac",
            "aac" to "audio/aac",
            "wma" to "audio/x-ms-wma",
            "m4a" to "audio/mp4",
            "opus" to "audio/opus",
            "midi" to "audio/midi",
            // Video
            "mp4" to "video/mp4",
            "mov" to "video/quicktime",
            "avi" to "video/x-msvideo",
            "mkv" to "video/x-matroska",
            "webm" to "video/webm",
            "wmv" to "video/x-ms-wmv",
            "flv" to "video/x-flv",
            "m4v" to "video/mp4",
            "3gp" to "video/3gpp",
            "ts" to "video/mp2t",
            "mpeg" to "video/mpeg"
        )

        /**
         * Maps media types to their storage directories
         * Pair contains (Environment directory constant, subdirectory name)
         */
        private val MEDIA_DIRECTORIES = mapOf(
            "image" to Pair(Environment.DIRECTORY_PICTURES, "Images"),
            "audio" to Pair(Environment.DIRECTORY_MUSIC, "Audios"),
            "video" to Pair(Environment.DIRECTORY_MOVIES, "Videos")
        )

        /**
         * Lookup map for quickly determining media type from file extension
         * e.g., "jpg" -> "image", "mp3" -> "audio"
         */
        private val EXTENSION_TO_MEDIA_TYPE = mutableMapOf<String, String>().apply {
            FILE_EXTENSIONS.forEach { (key, extensions) ->
                val mediaType = when {
                    MIME_TYPES[key]?.startsWith("image/") == true -> "image"
                    MIME_TYPES[key]?.startsWith("audio/") == true -> "audio"
                    MIME_TYPES[key]?.startsWith("video/") == true -> "video"
                    else -> "unknown"
                }
                extensions.forEach { ext -> put(ext, mediaType) }
            }
        }
    }

    private lateinit var downloadManager: DownloadManager
    private val downloadScope = CoroutineScope(Dispatchers.IO)
    private var downloadJobs = mutableListOf<Job>()

    /**
     * Initializes the service resources when created
     */
    override fun onCreate() {
        super.onCreate()
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        createNotificationChannel()
    }

    /**
     * Handles service start commands, including initiating downloads and processing cancellation
     *
     * @param intent The Intent used to start the service, containing download URLs
     * @param flags Additional data about this start request
     * @param startId A unique identifier for this start request
     * @return START_NOT_STICKY to prevent automatic restart if service is killed
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            CANCEL_ACTION -> {
                cancelDownloads()
                return START_NOT_STICKY
            }
        }

        val urls = intent?.getStringArrayListExtra("urls") ?: return START_NOT_STICKY
        if (urls.isEmpty()) return START_NOT_STICKY

        startForeground(NOTIFICATION_ID, buildNotification("Download started..."))

        val job = downloadScope.launch {
            try {
                downloadInBatches(urls)
                updateNotification("All downloads complete!")
                delay(NOTIFICATION_DELAY_MS)
            } catch (e: Exception) {
                Log.e("MediaDownloadService", "Download error: ${e.message}", e)
                updateNotification("Download error: ${e.message}")
                delay(NOTIFICATION_DELAY_MS)
            } finally {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        downloadJobs.add(job)

        return START_NOT_STICKY
    }

    /**
     * Cancels all active downloads and stops the service
     */
    private fun cancelDownloads() {
        downloadJobs.forEach { it.cancel() }
        downloadJobs.clear()
        updateNotification("Downloads canceled")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Downloads files in batches to limit concurrent operations
     *
     * @param urls List of URLs to download
     * @param batchSize Maximum number of concurrent downloads per batch
     */
    private suspend fun downloadInBatches(urls: List<String>, batchSize: Int = BATCH_SIZE) {
        val totalBatches = (urls.size + batchSize - 1) / batchSize

        urls.chunked(batchSize).forEachIndexed { batchIndex, batch ->
            Log.d("DownloadService", "Starting batch ${batchIndex + 1}/$totalBatches")
            updateNotification("Processing batch ${batchIndex + 1}/$totalBatches...")

            coroutineScope {
                batch.map { url ->
                    async {
                        downloadFile(url)
                    }
                }.awaitAll()
            }

            Log.d("DownloadService", "Batch ${batchIndex + 1}/$totalBatches complete")
        }
    }

    /**
     * Downloads a single file from the given URL
     *
     * @param url The URL of the file to download
     */
    private suspend fun downloadFile(url: String) {
        try {
            Log.d("DownloadService:: URL", url)
            val fileExtension = determineFileExtension(url)
            val mediaType = EXTENSION_TO_MEDIA_TYPE[fileExtension.lowercase()] ?: "unknown"
            val environmentDir = MEDIA_DIRECTORIES[mediaType] ?:
            Pair(Environment.DIRECTORY_DOWNLOADS, "Downloads")

            val fileName = "${url.substringAfterLast("/").substringBeforeLast(".")}.${fileExtension}"
            val fileDir = File(getExternalFilesDir(environmentDir.first), environmentDir.second).apply {
                if (!exists()) mkdirs()
            }
            val mediaFile = File(fileDir, fileName)

            if (mediaFile.exists()) {
                Log.d("DownloadService", "Skipping: $fileName (already exists)")
                updateNotification("Skipping: $fileName (already exists)")
                return
            }

            val request = buildDownloadRequest(url, mediaFile)
            val downloadId = downloadManager.enqueue(request)

            monitorDownload(downloadId, fileName)
            delay(DOWNLOAD_DELAY_MS)
        } catch (e: Exception) {
            Log.e("DownloadService", "Error downloading $url: ${e.message}", e)
            updateNotification("Error downloading: ${e.message}")
        }
    }

    /**
     * Updates the service's notification with new content
     *
     * @param content Text to display in the notification
     */
    private fun updateNotification(content: String) {
        val notification = buildNotification(content)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Determines the appropriate file extension from a URL
     *
     * @param url The URL to analyze
     * @return The standardized file extension (without dot)
     */
    private fun determineFileExtension(url: String): String {
        val urlLower = url.lowercase()
        // First try to extract extension from URL
        val urlExtension = urlLower.substringAfterLast('.', "")

        // Check if the extension exists in our mapping
        if (urlExtension.isNotEmpty()) {
            for ((ext, extensions) in FILE_EXTENSIONS) {
                if (extensions.contains(urlExtension)) {
                    return ext
                }
            }
        }

        // Default to jpg if not found
        return "jpg"
    }

    /**
     * Determines the MIME type for a file based on its URL
     *
     * @param url The URL to analyze
     * @return The appropriate MIME type string
     */
    private fun determineMimeType(url: String): String {
        val extension = determineFileExtension(url)
        return MIME_TYPES[extension] ?: "image/jpeg"
    }

    /**
     * Creates a download request for Android's DownloadManager
     *
     * @param url The URL to download
     * @param destinationFile The local file to save the download to
     * @return A configured DownloadManager.Request object
     */
    private fun buildDownloadRequest(url: String, destinationFile: File): DownloadManager.Request {
        val mimeType = determineMimeType(url)

        return DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setAllowedOverRoaming(false)
            setTitle(destinationFile.name)
            setDescription("Downloading...")
            setMimeType(mimeType)
            setDestinationUri(Uri.fromFile(destinationFile))
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
    }

    /**
     * Monitors the progress of a download and updates notifications accordingly
     *
     * @param downloadId The ID returned by DownloadManager.enqueue()
     * @param fileName The name of the file being downloaded for display purposes
     */
    @SuppressLint("Range")
    private suspend fun monitorDownload(downloadId: Long, fileName: String) {
        var downloading = true
        var previousStatus = -1

        while (downloading && isActive) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    // Only update notification if status has changed
                    if (status != previousStatus) {
                        val message = statusMessage(status)
                        updateNotification("$fileName: $message")
                        previousStatus = status
                    }

                    // Track download progress for larger files
                    if (status == DownloadManager.STATUS_RUNNING) {
                        val bytesDownloaded = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val bytesTotal = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                        if (bytesTotal > 0) {
                            val progress = (bytesDownloaded * 100 / bytesTotal).toInt()
                            if (progress % 20 == 0) { // Update every 20%
                                updateNotification("$fileName: Downloading $progress%")
                            }
                        }
                    }

                    if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                        downloading = false
                    }
                } else {
                    downloading = false
                }
            }

            delay(500)
        }
    }

    /**
     * Converts DownloadManager status codes to human-readable messages
     *
     * @param status The DownloadManager status code
     * @return Human-readable status description
     */
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

    /**
     * Creates a notification for the foreground service
     *
     * @param content Text to display in the notification
     * @return A configured Notification object
     */
    private fun buildNotification(content: String): Notification {
        val cancelIntent = Intent(this, MediaDownloadService::class.java).apply {
            action = CANCEL_ACTION
        }

        val pendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Media Download")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancel", pendingIntent)
            .build()
    }

    /**
     * Creates the notification channel required for Android O and above
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Media Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification channel for media downloads"
                setShowBadge(false)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Cleans up resources when the service is destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        downloadJobs.forEach { it.cancel() }
        downloadScope.cancel()
    }

    /**
     * Required for Service implementation, but we don't support binding
     */
    override fun onBind(p0: Intent?): IBinder? = null
}
