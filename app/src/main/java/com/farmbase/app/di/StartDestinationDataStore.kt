package com.farmbase.app.di

import android.content.Context
import com.farmbase.app.auth.datastore.repo.StartDestinationRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StartDestinationDataStore {

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context)= StartDestinationRepo(context)

}