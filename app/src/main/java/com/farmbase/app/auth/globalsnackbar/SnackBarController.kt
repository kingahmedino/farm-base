package com.farmbase.app.auth.globalsnackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackBarEvent(
    val message: String,
    val action: SnackBarAction? = null,
    val dismissAction: (() -> Unit)? = null  // New dismiss action
)

data class SnackBarAction(
    val name: String,
    val action: suspend () -> Unit
)

object SnackBarController {

    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    private val _dismissEvents = Channel<Unit>() // New channel for dismissing
    val dismissEvents = _dismissEvents.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarEvent) {
        _events.send(event)
    }

    suspend fun dismissSnackbar() {
        _dismissEvents.send(Unit) // Trigger dismissal event
    }
}