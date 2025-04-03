package com.farmbase.app.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.farmbase.app.repositories.IconsRepository
import javax.inject.Inject

class SyncWorkerFactory @Inject constructor(
    private val orchestrator: CouchbaseSyncOrchestrator,
    private val iconsRepository: IconsRepository
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

            IconDownloadWorker::class.java.name -> {
                IconDownloadWorker(appContext, workerParameters, iconsRepository)
            }
            else -> null
        }
    }
}
