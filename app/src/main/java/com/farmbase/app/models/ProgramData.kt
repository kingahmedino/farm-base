package com.farmbase.app.models

import com.google.gson.annotations.SerializedName

data class ProgramData(
    @SerializedName("program_name")
    val programName: String,
    @SerializedName("program_id")
    val programId: String,
    @SerializedName("icon")
    val icon: String,
)