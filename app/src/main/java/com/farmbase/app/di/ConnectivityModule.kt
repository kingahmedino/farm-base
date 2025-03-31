package com.farmbase.app.di

import android.content.Context
import com.farmbase.app.auth.internetconnectionobserver.AndroidConnectivityObserver
import com.farmbase.app.auth.internetconnectionobserver.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver {
        return AndroidConnectivityObserver(context)
    }
}