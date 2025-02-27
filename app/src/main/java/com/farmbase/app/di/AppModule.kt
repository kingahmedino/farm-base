package com.farmbase.app.di
import android.content.Context
import com.farmbase.app.database.FarmerDao
import com.farmbase.app.database.FarmBaseDatabase
import com.farmbase.app.repositories.FarmerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFarmerDatabase(@ApplicationContext context: Context): FarmBaseDatabase {
        return FarmBaseDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFarmerDao(database: FarmBaseDatabase): FarmerDao {
        return database.farmerDao()
    }

    @Provides
    @Singleton
    fun provideFarmerRepository(farmerDao: FarmerDao): FarmerRepository {
        return FarmerRepository(farmerDao)
    }
}
