package com.farmbase.app.auth.datastore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.auth.datastore.model.StartDestinationModel
import com.farmbase.app.auth.datastore.repo.StartDestinationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartDestinationViewModel @Inject constructor(private val startDestinationRepo: StartDestinationRepo): ViewModel() {

    ///// me
    val getData : StateFlow<StartDestinationModel> =
         startDestinationRepo.getDataStore().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = StartDestinationModel(null)
        )


    /////
    fun saveData(startDestinationModel: StartDestinationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            startDestinationRepo.saveDataStore(startDestinationModel)
        }
    }


}