package com.farmbase.app.models

import androidx.room.PrimaryKey

data class Farmer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val location: String,
    val specialtyCrops: String
)