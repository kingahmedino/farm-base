package com.farmbase.app.repositories

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.farmbase.app.database.CropDao
import com.farmbase.app.database.EmployeeDao
import com.farmbase.app.database.EquipmentDao
import com.farmbase.app.database.FarmerDao
import com.farmbase.app.database.HarvestDao
import com.farmbase.app.database.ProjectDao
import com.farmbase.app.database.StorageDao
import com.farmbase.app.models.Crop
import com.farmbase.app.models.Employee
import com.farmbase.app.models.Equipment
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.Harvest
import com.farmbase.app.models.Project
import com.farmbase.app.models.Storage
import com.farmbase.app.sync.SyncWorker
import kotlinx.coroutines.flow.Flow

class FarmerRepository(
    private val farmerDao: FarmerDao,
    private val cropDao: CropDao,
    private val harvestDao: HarvestDao,
    private val equipmentDao: EquipmentDao,
    private val employeeDao: EmployeeDao,
    private val projectDao: ProjectDao,
    private val storageDao: StorageDao,
) {
    val allFarmers: Flow<List<Farmer>> = farmerDao.getAllFarmers()

    suspend fun insertFarmer(farmer: Farmer) {
        farmerDao.insertFarmer(farmer)
        scheduleSync()
    }

    suspend fun updateFarmer(farmer: Farmer) {
        farmerDao.updateFarmer(farmer)
    }

    // Insert farmers with background sync
    suspend fun insertFarmers(farmers: List<Farmer>) {
        farmerDao.insertFarmers(farmers)
    }

    suspend fun insertCrops(crops: List<Crop>) {
        cropDao.insertCrops(crops)
    }

    suspend fun insertHarvests(harvests: List<Harvest>) {
        harvestDao.insertHarvests(harvests)
    }

    suspend fun insertEquipments(equipments: List<Equipment>) {
        equipmentDao.insertEquipments(equipments)
    }

    suspend fun insertEmployees(employees: List<Employee>) {
        employeeDao.insertEmployees(employees)
    }

    suspend fun insertProjects(projects: List<Project>) {
        projectDao.insertProjects(projects)
    }

    suspend fun insertStorages(storages: List<Storage>) {
        storageDao.insertStorages(storages)
    }

    // Sync worker for background operations
    fun scheduleSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(false)
                    .build()
            )
            .build()

        WorkManager.getInstance()
            .beginUniqueWork(
                "sync_database",
                ExistingWorkPolicy.REPLACE,
                syncRequest
            ).enqueue()
    }
}