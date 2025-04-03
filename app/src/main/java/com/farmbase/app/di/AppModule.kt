package com.farmbase.app.di

import android.content.Context
import com.farmbase.app.database.ActivityEntityDao
import com.farmbase.app.database.CropDao
import com.farmbase.app.database.EmployeeDao
import com.farmbase.app.database.EquipmentDao
import com.farmbase.app.database.FarmBaseDatabase
import com.farmbase.app.database.FarmerDao
import com.farmbase.app.database.HarvestDao
import com.farmbase.app.database.IconsDao
import com.farmbase.app.database.ProjectDao
import com.farmbase.app.database.StorageDao
import com.farmbase.app.database.couchbase.DBManager
import com.farmbase.app.database.RoleEntityDao
import com.farmbase.app.network.FormBuilderApiService
import com.farmbase.app.network.ProgramConfigApiService
import com.farmbase.app.repositories.ActivityEntityRepository
import com.farmbase.app.repositories.FarmerRepository
import com.farmbase.app.repositories.FormBuilderRepository
import com.farmbase.app.repositories.IconsRepository
import com.farmbase.app.repositories.ProgramConfigRepository
import com.farmbase.app.repositories.RoleRepository
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
    fun provideDBManager(@ApplicationContext context: Context): DBManager {
        return DBManager.getInstance(context)
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

    @Provides
    @Singleton
    fun provideFormBuilderRepository(
        formBuilderApiService: FormBuilderApiService
    ): FormBuilderRepository {
        return FormBuilderRepository(formBuilderApiService)
    }

    @Provides
    @Singleton
    fun provideProgramConfigRepository(
        programConfigApiService: ProgramConfigApiService
    ): ProgramConfigRepository {
        return ProgramConfigRepository(programConfigApiService)
    }


    @Provides
    @Singleton
    fun provideRoleEntityDao(database: FarmBaseDatabase): RoleEntityDao {
        return database.roleEntityDao()
    }

    @Provides
    @Singleton
    fun provideRoleEntityRepository(roleEntityDao: RoleEntityDao): RoleRepository {
        return RoleRepository(roleEntityDao)
    }

    @Provides
    @Singleton
    fun provideActivityEntityDao(database: FarmBaseDatabase): ActivityEntityDao {
        return database.activityEntityDao()
    }

    @Provides
    @Singleton
    fun provideActivityEntityRepository(activityEntityDao: ActivityEntityDao): ActivityEntityRepository {
        return ActivityEntityRepository(activityEntityDao)
    }

    @Provides
    @Singleton
    fun provideIconDao(database: FarmBaseDatabase): IconsDao {
        return database.iconsDao()
    }

    @Provides
    @Singleton
    fun provideIconRepository(iconDao: IconsDao): IconsRepository {
        return IconsRepository(iconDao)
    }
}
