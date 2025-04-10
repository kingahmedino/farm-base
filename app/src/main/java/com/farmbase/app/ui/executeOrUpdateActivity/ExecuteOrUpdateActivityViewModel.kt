package com.farmbase.app.ui.executeOrUpdateActivity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.farmbase.app.R
import com.farmbase.app.utils.ActivityCardItem
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExecuteOrUpdateActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
):ViewModel() {

    private val _activityCard = MutableStateFlow<ActivityCardItem?>(null)
    val activityCard = _activityCard.asStateFlow()

    // update selected activity card
    private val _selectedActivityCard = MutableStateFlow<ActivityCardItem?>(null)
    val selectedActivityCard = _selectedActivityCard.asStateFlow()

    private val _programList = MutableStateFlow(
        listOf(
            ActivityCardItem(
                id = context.getString(R.string.complete_activity),
                icon = R.drawable.ic_complete_activity,
                headerText = context.getString(R.string.complete_activity),
            )
            ,
            ActivityCardItem(
                id = context.getString(R.string.provide_status_update),
                icon = R.drawable.ic_provide_status_update,
                headerText = context.getString(R.string.provide_status_update),
            )
        )
    )
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

    fun updateSelectedActivityItem(activityCardItem: String) {
        val activityItem = Gson().fromJson(activityCardItem, ActivityCardItem::class.java)
        _activityCard.value = activityItem
    }
}
