package com.farmbase.app.utils

/**
 * Represents an activity card item used in the UI.
 * Each card contains an identifier, icon, text details, selection status, and an optional count.
 */
data class ActivityCardItem(
    // unique identifier for the activity card
    val id: String,
    // resource ID for the local icon
    val icon: Int,
    // optional URL for a remote icon
    val iconUrl: String? = null,
    // main title/header of the card
    val headerText: String,
    // optional description text for additional details
    val descText: String? = null,
    // type/category of activity
    val activityType: Constants.ActivityType? = null,
    // indicates if the card is currently selected
    val isSelected: Boolean = false,
    // optional count value associated with the card
    val count: Int = 0,
)