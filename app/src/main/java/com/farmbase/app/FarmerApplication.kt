package com.farmbase.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmbase.app.di.AppModule
import com.farmbase.app.sync.SyncOrchestrator
import com.farmbase.app.sync.SyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FarmerApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(
                    workerFactory = SyncWorkerFactory(
                        SyncOrchestrator(
                            AppModule.provideFarmerDatabase(
                                applicationContext
                            )
                        )
                    )
                )
                .build()
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                workerFactory = SyncWorkerFactory(
                    SyncOrchestrator(
                        AppModule.provideFarmerDatabase(
                            applicationContext
                        )
                    )
                )
            )
            .build()
}
