package com.farmbase.app.ui.farmerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.models.Farmer
import com.farmbase.app.repositories.FarmerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerListViewModel @Inject constructor(
   private val repository: FarmerRepository
) : ViewModel() {
    val allFarmers: StateFlow<List<Farmer>> = repository.allFarmers
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertFarmers() {
        val farmers = mutableListOf<Farmer>()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 0..1000) {
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
}