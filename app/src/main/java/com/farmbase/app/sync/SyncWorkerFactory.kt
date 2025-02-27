package com.farmbase.app.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class SyncWorkerFactory @Inject constructor(
    private val orchestrator: SyncOrchestrator,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> {
                SyncWorker(appContext, workerParameters, orchestrator)
            }
            else -> null
        }
    }
}
