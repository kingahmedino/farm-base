package com.farmbase.app.ui.selectProgram

import android.content.Context
import androidx.lifecycle.ViewModel
import com.farmbase.app.R
import com.farmbase.app.ui.widgets.ActivityItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectProgramViewModel @Inject constructor(
    @ApplicationContext private val context: Context
):ViewModel() {
    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled

    private val _programList = MutableStateFlow(
        listOf(
            ActivityItem(icon = R.drawable.ic_personal_info, headerText = "Poultry OSO"),
            ActivityItem(icon = R.drawable.ic_personal_info, headerText = "ELPM"),
            ActivityItem(icon = R.drawable.ic_my_schedule, headerText = "Poultry Mother Officer")
        )
    )
    val programList: StateFlow<List<ActivityItem>> = _programList.asStateFlow()

    // Function to update selection
    fun updateSelectedCard(index: Int) {

        // Update the selected list
        _programList.update { list ->
            list.mapIndexed { i, item ->
                item.copy(isSelected = i == index && !item.isSelected)
            }
        }
        // update button state based on whether any item is selected
        val isAnySelected = _programList.value.any { it.isSelected }
        updateButtonState(isAnySelected)
    }

    private fun updateButtonState(enabled: Boolean) {
        _isButtonEnabled.value = enabled
    }

}
