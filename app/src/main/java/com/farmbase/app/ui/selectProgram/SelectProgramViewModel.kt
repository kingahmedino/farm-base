package com.farmbase.app.ui.selectProgram

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.R
import com.farmbase.app.models.ActivityEntity
import com.farmbase.app.models.ProgramConfig
import com.farmbase.app.models.ProgramData
import com.farmbase.app.models.RoleEntity
import com.farmbase.app.repositories.ActivityEntityRepository
import com.farmbase.app.repositories.RoleRepository
import com.farmbase.app.ui.formBuilder.utils.Resource
import com.farmbase.app.useCase.GetProgramConfigByIDUseCase
import com.farmbase.app.useCase.GetProgramDataByRolesUseCase
import com.farmbase.app.utils.ActivityCardItem
import com.farmbase.app.utils.Constants
import com.farmbase.app.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectProgramViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val programDataUseCase: GetProgramDataByRolesUseCase,
    private val programConfigUseCase: GetProgramConfigByIDUseCase,
    private val roleRepository: RoleRepository,
    private val activityEntityRepository: ActivityEntityRepository
):ViewModel() {
    private val roles =  listOf("67e00ad59b2b98774577c62a", "67e038e0b4320cde3f1cc247" )
    private val _programData = MutableStateFlow<Resource<List<ProgramData>>>(Resource.Loading())
    val programData: StateFlow<Resource<List<ProgramData>>> = _programData.asStateFlow()

    private val _programConfig = MutableStateFlow<Resource<ProgramConfig>>(Resource.Error(""))
    val programConfig: StateFlow<Resource<ProgramConfig>> = _programConfig.asStateFlow()

    init {
        fetchProgramDetails()
    }

    /** This function fetches the details of available programs
     * and updates the program list state.
     * */
    private fun fetchProgramDetails() {
        viewModelScope.launch {
            // set the program data to a loading state while the data is being fetched
            _programData.value = Resource.Loading()

            // invoke the use case to fetch the program data based on the logged in users role
            _programData.value = programDataUseCase.invoke(roles)

            // if the fetched data is successful, proceed to process the program list
            (_programData.value as? Resource.Success)?.data?.let { programList ->
                // create a mutable list to accumulate all the program items
                val updatedProgramList = mutableListOf<ActivityCardItem>()

                // iterates through each program data item from the fetched list
                programList.forEach { programData ->
                    // add each program data to the updated list.
                    updatedProgramList.add(
                        ActivityCardItem(
                            id = programData.programId,
                            iconUrl = programData.icon.trim(),
                            icon = R.drawable.ic_back,
                            headerText = programData.programName
                        )
                    )
                }

                // update the LiveData with the complete list of programs.
                _programList.value = updatedProgramList
            }
        }
    }


    // update selected activity card
    private val _selectedActivityCard = MutableStateFlow<ActivityCardItem?>(null)
    val selectedActivityCard = _selectedActivityCard.asStateFlow()

    private val _programList = MutableStateFlow<List<ActivityCardItem>>(emptyList())
    val programList: StateFlow<List<ActivityCardItem>> = _programList.asStateFlow()

    /** Updates the selected activity card when a card is clicked
     *  If the clicked card is already selected, set it to null.
     *  Otherwise, it sets the clicked card as the selected one.
     */
    fun updateSelectedCard(selectedCard: ActivityCardItem) {
        when {
            selectedCard == _selectedActivityCard.value -> {
                _selectedActivityCard.value = null
            }
            else -> {
                _selectedActivityCard.value = selectedCard
            }
        }
    }

    /** Save selected program id using shared preference */
    fun saveProgramIdToSharedPrefs() {
        _selectedActivityCard.value?.let {
            SharedPreferencesManager(context = context ).encryptedPut(
                Constants.SELECTED_PROGRAM_ID, it.id) }
    }

    fun saveData(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _programConfig.value = Resource.Loading()
            _programConfig.value = programConfigUseCase.invoke("67a5a8ef7b4f3b17d961564c")

            (_programConfig.value as? Resource.Success)?.data?.let { programConfigs ->
                val roles = programConfigs.roles.map {
                    RoleEntity(
                        roleId = it.roleId,
                        name = it.name,
                        abbreviation = it.abbreviation,
                        childPortfolio = it.childPortfolios.joinToString()
                    )
                }

                val activities = programConfigs.activities.map {
                    ActivityEntity(
                        activityId = it.activityId,
                        name = it.name,
                        entity = it.entity.joinToString(),
                        entityType = it.entityType,
                        facialVerificationFlag = it.facialVerificationFlag,
                        locationCheckFlag = it.locationCheckFlag,
                        formId = it.formId,
                        daysToLate = it.daysToLate,
                        daysToVeryLate = it.daysToVeryLate,
                        scheduledActivityFlag = it.scheduledActivityFlag,

                    )
                }

                // Inserting data on the IO dispatcher
                withContext(Dispatchers.IO) {
                    roleRepository.insertFarmer(roles)
                    activityEntityRepository.insertActivities(activities)
                }

                // Call the success callback after insertion on the main thread
                onSuccess()
            }
        }
    }

}
