package com.farmbase.app.auth.sessionManager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
//
//    private var lastInteractionTime: Long = System.currentTimeMillis()
//    private val handler = Handler(Looper.getMainLooper())
//    private val sessionTimeoutRunnable = Runnable {
//        checkSessionTimeout()
//    }
//
//    fun onUserInteraction() {
//        lastInteractionTime = System.currentTimeMillis()
//        restartSessionTimer()
//    }
//
//    fun startSessionTimer() {
//        handler.postDelayed(sessionTimeoutRunnable, SESSION_TIMEOUT_DURATION)
//    }
//
//    fun stopSessionTimer() {
//        handler.removeCallbacks(sessionTimeoutRunnable)
//    }
//
//    private fun restartSessionTimer() {
//        stopSessionTimer()
//        startSessionTimer()
//    }
//
//    private fun checkSessionTimeout() {
//        val currentTime = System.currentTimeMillis()
//        if (currentTime - lastInteractionTime > SESSION_TIMEOUT_DURATION) {
//
//            Toast.makeText(context, "User inactive for 30 seconds", Toast.LENGTH_SHORT).show()
//
//        }
//    }
//
//    companion object {
//        private const val SESSION_TIMEOUT_DURATION = 30000L // 30 seconds
//    }
//}

class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var lastInteractionTime: Long = System.currentTimeMillis()
    private val handler = Handler(Looper.getMainLooper())
    private val sessionTimeoutRunnable = Runnable {
        checkSessionTimeout()
    }

    private val _sessionTimeoutFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionTimeoutFlow: SharedFlow<Unit> = _sessionTimeoutFlow

    fun onUserInteraction() {
        lastInteractionTime = System.currentTimeMillis()
        restartSessionTimer()
    }

    fun startSessionTimer() {
        handler.postDelayed(sessionTimeoutRunnable, SESSION_TIMEOUT_DURATION)
    }

    fun stopSessionTimer() {
        handler.removeCallbacks(sessionTimeoutRunnable)
    }

    private fun restartSessionTimer() {
        stopSessionTimer()
        startSessionTimer()
    }

    private fun checkSessionTimeout() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInteractionTime > SESSION_TIMEOUT_DURATION) {
            _sessionTimeoutFlow.tryEmit(Unit) // Emit event instead of showing Toast
        }
    }

    companion object {
        private const val SESSION_TIMEOUT_DURATION = 30000L // 30 seconds
    }
}

