package com.farmbase.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmbase.app.auth.koin.connectivitySessionModule
import com.farmbase.app.auth.koin.viewModelModule
import com.farmbase.app.di.AppModule
import com.farmbase.app.repositories.FormBuilderRepository
import com.farmbase.app.repositories.IconsRepository
import com.farmbase.app.sync.CouchbaseSyncOrchestrator
import com.farmbase.app.sync.SyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import javax.inject.Inject

@HiltAndroidApp
class FarmerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var formBuilderRepository: FormBuilderRepository
    @Inject
    lateinit var iconsRepository: IconsRepository

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
                        ),
                        iconsRepository = iconsRepository
                    )
                )
                .build()
        )

        // koin
        startKoin {
            androidContext(this@FarmerApplication)
            modules(connectivitySessionModule, viewModelModule) // add more modules here if needed
        }
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
                    ),
                    iconsRepository
                )
            )
            .build()
}
