package com.farmbase.app.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val orchestrator: SyncOrchestrator
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            orchestrator.startSync()
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
