package com.farmbase.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.SyncMetadata

@Database(entities = [Farmer::class, SyncMetadata::class], version = 1)
abstract class FarmBaseDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun syncMetadataDao(): SyncMetadataDao

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