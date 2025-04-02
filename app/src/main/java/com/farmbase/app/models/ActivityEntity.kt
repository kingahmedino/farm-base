package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "activities")
data class ActivityEntity(
    @SerializedName("activity_id")
    @PrimaryKey
    val activityId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("entity")
    val entity: String,
    @SerializedName("entity_type")
    val entityType: String,
    @SerializedName("facial_verification_flag")
    val facialVerificationFlag: Int,
    @SerializedName("location_check_flag")
    val locationCheckFlag: Int,
    @SerializedName("form_id")
    val formId: String,
    @SerializedName("days_to_late")
    val daysToLate: Int,
    @SerializedName("days_to_very_late")
    val daysToVeryLate: Int,
    @SerializedName("scheduled_activity_flag")
    val scheduledActivityFlag: Int
)