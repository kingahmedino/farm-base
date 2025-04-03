package com.farmbase.app.repositories

import com.farmbase.app.database.IconsDao
import com.farmbase.app.models.Icons
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
    }
}