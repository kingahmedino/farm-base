package com.farmbase.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farmbase.app.models.ActivityEntity
import com.farmbase.app.models.Crop
import com.farmbase.app.models.Employee
import com.farmbase.app.models.Equipment
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.Harvest
import com.farmbase.app.models.Project
import com.farmbase.app.models.RoleEntity
import com.farmbase.app.models.Storage
import com.farmbase.app.models.SyncMetadata

@Database(
    entities = [
        Farmer::class,
        Crop::class,
        Harvest::class,
        Employee::class,
        Equipment::class,
        Project::class,
        Storage::class,
        SyncMetadata::class,
        RoleEntity::class,
        ActivityEntity::class,
               ],
    version = 1
)
abstract class FarmBaseDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun cropDao(): CropDao
    abstract fun harvestDao(): HarvestDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun projectDao(): ProjectDao
    abstract fun storageDao(): StorageDao
    abstract fun syncMetadataDao(): SyncMetadataDao
    abstract fun roleEntityDao(): RoleEntityDao
    abstract fun activityEntityDao(): ActivityEntityDao

    companion object {
        @Volatile
        private var INSTANCE: FarmBaseDatabase? = null

        fun getDatabase(context: Context): FarmBaseDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FarmBaseDatabase::class.java,
                    "farmbase_database"
                )
                    .enableMultiInstanceInvalidation()
                    .build().also { INSTANCE = it }
            }
        }
    }
}