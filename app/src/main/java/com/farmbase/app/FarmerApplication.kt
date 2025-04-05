package com.farmbase.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.farmbase.app.di.AppModule
import com.farmbase.app.network.ktor.KtorClient
import com.farmbase.app.network.ktor.SimpleTokenProvider
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


        KtorClient.configure(
            tokenProvider = SimpleTokenProvider(
                refreshCallback = {
                    // Example token refresh logic
                    try {
                        // Make a refresh token API call
                        val result = KtorClient.post<RefreshTokenResponse>(
                            url = "https://api.example.com/auth/refresh",
                            body = mapOf("refreshToken" to getRefreshToken()),
                            requiresAuth = false
                        )

                        result.fold(
                            onSuccess = { response ->
                                // Save the new refresh token
                                saveRefreshToken(response.refreshToken)
                                // Return the new access token
                                response.accessToken
                            },
                            onFailure = {
                                // Token refresh failed
                                null
                            }
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            ),
            debug = BuildConfig.DEBUG
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

    private fun getRefreshToken(): String {
        // Get refresh token from secure storage
        return "refresh-token" // Replace with actual implementation
    }

    private fun saveRefreshToken(token: String) {
        // Save refresh token to secure storage
        // This is just a placeholder implementation
    }

    data class RefreshTokenResponse(
        val accessToken: String,
        val refreshToken: String
    )
}
