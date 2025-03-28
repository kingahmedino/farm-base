package com.farmbase.app.ui.homepage

import android.content.Context
import androidx.lifecycle.ViewModel
import com.farmbase.app.R
import com.farmbase.app.utils.ActivityCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    @ApplicationContext private val context: Context
):ViewModel() {

    // update selected activity card
    private val _selectedActivityCard = MutableStateFlow<Any?>(null)
    val selectedActivityCard = _selectedActivityCard.asStateFlow()

    private val _portfolioActivityList = MutableStateFlow(
        listOf(
            ActivityCardItem(id = "1", icon = R.drawable.ic_personal_info, headerText = "Poultry OSO"),
            ActivityCardItem(
                id = "2",
                icon = R.drawable.ic_personal_info,
                headerText = "ELPM",
                count = 3
            ),
            ActivityCardItem(
                id = "3",
                icon = R.drawable.ic_my_schedule,
                headerText = "Poultry Mother Officer"
            )
        )
    )
    val portfolioActivityList: StateFlow<List<ActivityCardItem>> = _portfolioActivityList.asStateFlow()

    private val _activityList = MutableStateFlow(
        listOf(
            ActivityCardItem(id = "4", icon = R.drawable.ic_back, headerText = "Poultry OSO"),
            ActivityCardItem(id = "5", icon = R.drawable.ic_back, headerText = "ELPM", count = 13),
            ActivityCardItem(id = "6", icon = R.drawable.ic_back, headerText = "Poultry Mother Officer")
        )
    )
    val activityList: StateFlow<List<ActivityCardItem>> = _activityList.asStateFlow()

    private val _portfolioList = MutableStateFlow(
        listOf(
            ActivityCardItem(
                id = "7",
                icon = R.drawable.ic_back,
                headerText = context.getString(R.string.my_portfolio)
            )
        )
    )
    val portfolioList: StateFlow<List<ActivityCardItem>> = _portfolioList.asStateFlow()

    private val _historyList = MutableStateFlow<List<ActivityCardItem>>(emptyList())
    val historyList: StateFlow<List<ActivityCardItem>> = _historyList.asStateFlow()

    /**
     * update activity card selected
     * @param card expects activity card selected
     * */
    fun updateActivityCardSelected(card: Any?) {
        when {
            card == _selectedActivityCard.value -> {
                _selectedActivityCard.value = null
            }
            else -> {
                _selectedActivityCard.value = card
            }
        }

    }

    /**
     * update activity card selected
     *
     * @param itemSelected: expects activity card selected
     * @param item: expects activity card to be compared against
     * @return expects true if the card is selected and false if the no activity card has been selected or it isn't the correct activity card
     * */
    fun cardStateChange(itemSelected: Any?, item: Any?): Boolean {
        return when (itemSelected) {
            is ActivityCardItem -> {
                item == itemSelected
            }
            else -> {
                false
            }
        }
    }

    /**
     * Updates the history list with activity card items based on the provided role.
     * This function creates a list of activity items, using the role to format the header text.*
     * @param role the role to be displayed in the header text of the activity cards.
     */
     fun updateHistoryList(role: String) {
         _historyList.value = listOf(
             ActivityCardItem(
                 id = "8",
                 icon = R.drawable.ic_back,
                 headerText = context.getString(R.string.user_dashboard, role)
             ),
             ActivityCardItem(
                 id = "9",
                 icon = R.drawable.ic_back,
                 headerText = context.getString(R.string.user_history, role)
             ),
         )
    }
}
