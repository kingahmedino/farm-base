package com.farmbase.app.ui.farmerlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.ListenerToken
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.Ordering
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.Result
import com.couchbase.lite.SelectResult
import com.farmbase.app.database.couchbase.DBManager
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FarmerListViewModel @Inject constructor(
    private val repository: FarmerRepository,
    private val dbManager: DBManager,
) : ViewModel() {
    private val database: Database = dbManager.getDatabase()
    private var formsChangeListener: ListenerToken? = null
    private var queryChangeListeners = mutableListOf<ListenerToken?>()

    init {
        startLiveQuery()
    }

    val allFarmers: StateFlow<List<Farmer>> = repository.allFarmers
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _resultsState = MutableStateFlow<List<Result>>(emptyList())
    val resultsState = _resultsState.asStateFlow()

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

    private fun startLiveQuery() {
        try {
            val collection = database.createCollection("forms")
            // Get all forms in app
            val query = QueryBuilder
                .select(
                    SelectResult.property("id"),
                    SelectResult.property("name"),
                    SelectResult.property("priority")
                )
                .from(DataSource.collection(collection))

            formsChangeListener = query.addChangeListener { formsChange ->
                if (formsChange.error == null) {

                    for (result in formsChange.results!!) {
                        val formName = result.getString("name") ?: continue
                        val formCollection = database.createCollection(formName)

                        val formQuery = QueryBuilder
                            .select(SelectResult.all())
                            .from(DataSource.collection(formCollection))

                        queryChangeListeners.add(
                            formQuery.addChangeListener { change ->
                                if (change.error == null) {
                                    val results = change.results!!.allResults()

                                    // Update state flow
                                    _resultsState.value = results
                                    syncData()
                                } else {
                                    Log.e("LiveQuery", "Error in live query", change.error)
                                }
                            }
                        )
                    }
                } else {
//                    _uiState.value = UiState.Error("Query error: ${change.error.message}")
                    Log.e("LiveQuery", "Error in live query", formsChange.error)
                }
            }

            // Register a change listener for them
            query.execute().use { resultSet ->

            }

        } catch (e: Exception) {
            Log.e("LiveQuery", "Failed to start live query", e)
        }
    }

    override fun onCleared() {
        queryChangeListeners.map { e -> e?.remove() }
        super.onCleared()
    }
}
