package com.farmbase.app.di

import android.content.Context
import com.farmbase.app.database.CropDao
import com.farmbase.app.database.EmployeeDao
import com.farmbase.app.database.EquipmentDao
import com.farmbase.app.database.FarmerDao
import com.farmbase.app.database.FarmBaseDatabase
import com.farmbase.app.database.HarvestDao
import com.farmbase.app.database.ProjectDao
import com.farmbase.app.database.StorageDao
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
    fun provideCropDao(database: FarmBaseDatabase): CropDao {
        return database.cropDao()
    }

    @Provides
    @Singleton
    fun provideHarvestDao(database: FarmBaseDatabase): HarvestDao {
        return database.harvestDao()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(database: FarmBaseDatabase): EmployeeDao {
        return database.employeeDao()
    }

    @Provides
    @Singleton
    fun provideEquipmentDao(database: FarmBaseDatabase): EquipmentDao {
        return database.equipmentDao()
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: FarmBaseDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideStorageDao(database: FarmBaseDatabase): StorageDao {
        return database.storageDao()
    }

    @Provides
    @Singleton
    fun provideFarmerRepository(
        farmerDao: FarmerDao,
        cropDao: CropDao,
        harvestDao: HarvestDao,
        employeeDao: EmployeeDao,
        equipmentDao: EquipmentDao,
        projectDao: ProjectDao,
        storageDao: StorageDao
    ): FarmerRepository {
        return FarmerRepository(
            farmerDao,
            cropDao,
            harvestDao,
            equipmentDao,
            employeeDao,
            projectDao,
            storageDao,
        )
    }
}
