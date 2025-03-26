package com.farmbase.app.ui.homepage

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
class HomepageViewModel @Inject constructor(
    @ApplicationContext private val context: Context
):ViewModel() {
    private val _role = MutableStateFlow("")
    val role: StateFlow<String> = _role

    // update selected activity card
    private val _selectedActivityCard = MutableStateFlow<Any?>(null)
    val selectedActivityCard = _selectedActivityCard.asStateFlow()

    private val _portfolioActivityList = MutableStateFlow(
        listOf(
            ActivityItem(id = 1, icon = R.drawable.ic_personal_info, headerText = "Poultry OSO"),
            ActivityItem(
                id = 2,
                icon = R.drawable.ic_personal_info,
                headerText = "ELPM",
                count = 3
            ),
            ActivityItem(
                id = 3,
                icon = R.drawable.ic_my_schedule,
                headerText = "Poultry Mother Officer"
            )
        )
    )
    val portfolioActivityList: StateFlow<List<ActivityItem>> = _portfolioActivityList.asStateFlow()

    private val _activityList = MutableStateFlow(
        listOf(
            ActivityItem(id = 4, icon = R.drawable.ic_back, headerText = "Poultry OSO"),
            ActivityItem(id = 5, icon = R.drawable.ic_back, headerText = "ELPM", count = 13),
            ActivityItem(id = 6, icon = R.drawable.ic_back, headerText = "Poultry Mother Officer")
        )
    )
    val activityList: StateFlow<List<ActivityItem>> = _activityList.asStateFlow()

    private val _portfolioList = MutableStateFlow(
        listOf(
            ActivityItem(
                id = 7,
                icon = R.drawable.ic_back,
                headerText = context.getString(R.string.my_portfolio)
            )
        )
    )
    val portfolioList: StateFlow<List<ActivityItem>> = _portfolioList.asStateFlow()

    private val _historyList = MutableStateFlow(
        listOf(
            ActivityItem(
                id = 8,
                icon = R.drawable.ic_back,
                headerText = context.getString(R.string.user_dashboard, role.value)
            ),
            ActivityItem(
                id = 9,
                icon = R.drawable.ic_back,
                headerText = context.getString(R.string.user_history, role.value)
            ),
        )
    )
    val historyList: StateFlow<List<ActivityItem>> = _historyList.asStateFlow()

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
            is ActivityItem -> {
                item == itemSelected
            }
            else -> {
                false
            }
        }
    }

    fun updateRole(role: String) {
        _role.value = role
    }
}
