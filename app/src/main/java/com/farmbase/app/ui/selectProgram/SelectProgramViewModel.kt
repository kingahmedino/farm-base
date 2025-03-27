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
import javax.inject.Inject

@HiltViewModel
class SelectProgramViewModel @Inject constructor(
    @ApplicationContext private val context: Context
):ViewModel() {
    // update selected activity card
    private val _selectedActivityCard = MutableStateFlow<ActivityItem?>(null)
    val selectedActivityCard = _selectedActivityCard.asStateFlow()

    private val _programList = MutableStateFlow(
        listOf(
            ActivityItem(id = 1, icon = R.drawable.ic_personal_info, headerText = "Poultry Mother Unit Program"),
            ActivityItem(id = 2,icon = R.drawable.ic_personal_info, headerText = "Maize Program"),
        )
    )
    val programList: StateFlow<List<ActivityItem>> = _programList.asStateFlow()

    /** Updates the selected activity card when a card is clicked
     *  If the clicked card is already selected, set it to null.
     *  Otherwise, it sets the clicked card as the selected one.
     */
    fun updateSelectedCard(selectedCard: ActivityItem) {
        when {
            selectedCard == _selectedActivityCard.value -> {
                _selectedActivityCard.value = null
            }
            else -> {
                _selectedActivityCard.value = selectedCard
            }
        }
    }
}
