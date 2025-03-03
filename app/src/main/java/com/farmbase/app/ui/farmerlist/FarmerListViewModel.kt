package com.farmbase.app.ui.farmerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.models.Crop
import com.farmbase.app.models.Employee
import com.farmbase.app.models.Equipment
import com.farmbase.app.models.Farmer
import com.farmbase.app.models.Harvest
import com.farmbase.app.models.Project
import com.farmbase.app.models.Storage
import com.farmbase.app.repositories.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FarmerListViewModel @Inject constructor(
   private val repository: FarmerRepository
) : ViewModel() {
    val allFarmers: StateFlow<List<Farmer>> = repository.allFarmers
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun syncData() {
        repository.scheduleSync()
    }

    fun insertFarmers() {
        val farmers = mutableListOf<Farmer>()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val farmer = Farmer(
                    name = "Farmer $i",
                    email = "farmer$i@gmail.com",
                    phoneNumber = "0901920192$i",
                    location = "Farmer $i Location",
                    specialtyCrops = "Farmer $i specialty",
                    profilePictureUrl = "Farmer $i profile picture"
                )

                farmers.add(farmer)
            }

            repository.insertFarmers(farmers)
        }
    }

    fun insertCrops() {
        val classOfFoods = listOf("Carbs", "Protein", "Fats", "Minerals", "Vitamins", "Oils")

        val crops = mutableListOf<Crop>()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val crop = Crop(
                    name = "Crop $i",
                    classOfFood = classOfFoods.random(),
                    image = "https://example.com"
                )

                crops.add(crop)
            }

            repository.insertCrops(crops)
        }
    }

    fun insertEmployee() {
        val employees = mutableListOf<Employee>()
        val names = listOf("Femi", "Deji", "Sola", "Wale", "Dapo", "Lola")
        val locations = listOf("Lagos", "Abuja", "Kano", "Kebbi", "Sokoto", "Kaduna")
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val employee = Employee(
                    name = "${names.random()} $i",
                    location = locations.random(),
                    profilePicture = "https://example.com"
                )

                employees.add(employee)
            }

            repository.insertEmployees(employees)
        }
    }

    fun insertEquipments() {
        val equipments = mutableListOf<Equipment>()
        val equipmentTypes = listOf("Hoe", "Tractor", "Cutlass", "Axe", "Dagger", "Watering Can")
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val equipment = Equipment(
                    name = "Equipment $i",
                    type = equipmentTypes.random(),
                    image = "https://example.com"
                )

                equipments.add(equipment)
            }

            repository.insertEquipments(equipments)
        }
    }

    fun insertProjects() {
        val projects = mutableListOf<Project>()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val harvest = Project(
                    name = "Project $i",
                    duration = Random.nextLong(),
                )

                projects.add(harvest)
            }

            repository.insertProjects(projects)
        }
    }

    fun insertStorages() {
        val storages = mutableListOf<Storage>()
        val locations =
            listOf("Lagos", "Kano", "Abuja", "Katsina", "Nasarrawa", "Ogun")
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val storage = Storage(
                    name = "Storage $i",
                    location = locations.random(),
                    imageOfStorage = "https://example.com"
                )

                storages.add(storage)
            }

            repository.insertStorages(storages)
        }
    }

    fun insertHarvests() {
        val harvests = mutableListOf<Harvest>()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..5000) {
                val harvest = Harvest(
                    cropName = "Harvest $i",
                    duration = "4 years",
                )

                harvests.add(harvest)
            }

            repository.insertHarvests(harvests)
        }
    }
}