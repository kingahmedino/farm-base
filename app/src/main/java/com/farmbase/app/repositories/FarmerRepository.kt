package com.farmbase.app.repositories

import com.farmbase.app.database.FarmerDao
import com.farmbase.app.models.Farmer
import kotlinx.coroutines.flow.Flow

class FarmerRepository(private val farmerDao: FarmerDao) {
    val allFarmers: Flow<List<Farmer>> = farmerDao.getAllFarmers()

    suspend fun insertFarmer(farmer: Farmer) {
        farmerDao.insertFarmer(farmer)
    }

    suspend fun updateFarmer(farmer: Farmer) {
        farmerDao.updateFarmer(farmer)
    }

    suspend fun getFarmerById(id: Int): Farmer? {
        return farmerDao.getFarmerById(id)
    }
}