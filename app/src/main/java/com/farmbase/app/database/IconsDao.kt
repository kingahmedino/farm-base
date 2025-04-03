package com.farmbase.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.farmbase.app.models.Icons
import com.farmbase.app.utils.Constants

@Dao
interface IconsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIcons(icons: List<Icons>)

    @Query("Select * from icons where downloadStatus = :downloadStatus")
    suspend fun getAllIconsToDownload(downloadStatus: Constants.IconsDownloadStatus): List<Icons>

    @Query("UPDATE icons SET downloadStatus = :status WHERE iconId = :iconId")
    suspend fun updateIconStatus(iconId: String, status: Constants.IconsDownloadStatus)

    @Query("Delete from icons")
    suspend fun deleteAllIcons()

    @Transaction
    suspend fun replaceIcons(icons: List<Icons>) {
        deleteAllIcons()
        insertIcons(icons)
    }
}