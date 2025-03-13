package com.farmbase.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmbase.app.di.AppModule
import com.farmbase.app.repositories.FormBuilderRepository
import com.farmbase.app.sync.CouchbaseSyncOrchestrator
import com.farmbase.app.sync.SyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FarmerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var formBuilderRepository: FormBuilderRepository

    override fun onCreate() {
        super.onCreate()

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(
                    workerFactory = SyncWorkerFactory(
                        CouchbaseSyncOrchestrator(
                            AppModule.provideDBManager(
                                applicationContext
                            ),
                            formBuilderRepository
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
                    CouchbaseSyncOrchestrator(
                        AppModule.provideDBManager(
                            applicationContext
                        ),
                        formBuilderRepository
                    )
                )
            )
            .build()
}
