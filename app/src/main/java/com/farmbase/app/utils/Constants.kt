package com.farmbase.app.utils

object Constants {
    const val SELECTED_PROGRAM_ID = "selected_program_id"

    val SPECIALTY_CROPS = listOf(
        "Herbs",
        "Lettuce",
        "Microgreens",
        "Mushrooms",
        "Tomatoes",
        "Carrots",
        "Beets",
        "Berries",
        "Peppers",
        "Flowers"
    )

    enum class ActivityType {
        PORTFOLIO_ACTIVITY,
        ACTIVITY,
        PORTFOLIO,
        HISTORY
    }
}