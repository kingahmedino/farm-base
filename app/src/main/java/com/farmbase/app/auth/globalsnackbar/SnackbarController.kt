package com.farmbase.app.auth.globalsnackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val dismissAction: (() -> Unit)? = null  // New dismiss action
)

data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit
)

object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    private val _dismissEvents = Channel<Unit>() // New channel for dismissing
    val dismissEvents = _dismissEvents.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    suspend fun dismissSnackbar() {
        _dismissEvents.send(Unit) // Trigger dismissal event
    }
}