package com.farmbase.app.repositories

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.farmbase.app.database.IconsDao
import com.farmbase.app.models.Icons
import com.farmbase.app.sync.IconDownloadWorker
import com.farmbase.app.sync.SyncWorker
import com.farmbase.app.utils.Constants

class IconsRepository(
    private val iconsDao: IconsDao,
) {
    suspend fun insertIcons(icons: List<Icons>) {
        iconsDao.insertIcons(icons)
    }

    suspend fun getAllIconsToDownload(): List<Icons> {
        return iconsDao.getAllIconsToDownload(Constants.IconsDownloadStatus.IN_VIEW)
    }

    suspend fun replaceIcons(icons: List<Icons>){
        iconsDao.replaceIcons(icons)
        startIconDownload()
    }

    suspend fun updateIconStatus(icoId: String, status: Constants.IconsDownloadStatus) {
        return iconsDao.updateIconStatus(icoId, status)
    }

    private fun startIconDownload() {
        val syncRequest = OneTimeWorkRequestBuilder<IconDownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(false)
                    .build()
            )
            .build()

        WorkManager.getInstance()
            .beginUniqueWork(
                "icon_download",
                ExistingWorkPolicy.REPLACE,
                syncRequest
            ).enqueue()
    }
}