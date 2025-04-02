package com.farmbase.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "roles")
data class RoleEntity (
    @PrimaryKey
    @SerializedName("role_id")
    val roleId: String,
    val name: String,
    val abbreviation: String,
    @SerializedName("child_portfolios")
    val childPortfolio: String
)