package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farmbase.app.utils.Constants
import com.google.gson.annotations.SerializedName

@Entity(tableName = "icons")
data class Icons(
    @SerializedName("icon_id")
    @PrimaryKey
    val iconId: String,
    @SerializedName("icon_url")
    val iconUrl: String,
    @SerializedName("download_status")
    val downloadStatus: Constants.IconsDownloadStatus = Constants.IconsDownloadStatus.IN_VIEW,
)