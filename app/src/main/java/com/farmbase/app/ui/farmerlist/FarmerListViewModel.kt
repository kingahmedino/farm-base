package com.farmbase.app.ui.farmerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.models.Farmer
import com.farmbase.app.repositories.FarmerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FarmerListViewModel(repository: FarmerRepository) : ViewModel() {
    val allFarmers: StateFlow<List<Farmer>> = repository.allFarmers
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}