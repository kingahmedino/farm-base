package com.farmbase.app.ui.selectHomepage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.R
import com.farmbase.app.utils.ActivityCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SelectHomepageViewModel @Inject constructor(
    @ApplicationContext private val context: Context
):ViewModel() {
    // update selected activity card
    private val _selectedActivityCard = MutableStateFlow<ActivityCardItem?>(null)
    val selectedActivityCard = _selectedActivityCard.asStateFlow()

    private val _mainList = MutableStateFlow(
        listOf(
            ActivityCardItem(id = "1",
                icon = R.drawable.ic_learning_videos,
                headerText = context.getString(R.string.learning_videos),
                descText = context.getString(R.string.learning_videos_desc)
            ),
            ActivityCardItem(id = "2",
                icon = R.drawable.ic_my_homepage,
                headerText = context.getString(R.string.my_homepage),
                descText = context.getString(R.string.my_homepage_desc)),
        )
    )
    private val mainList: StateFlow<List<ActivityCardItem>> = _mainList.asStateFlow()


    private val _entityList = MutableStateFlow(
        listOf(
            ActivityCardItem(id = "3",
                icon = R.drawable.ic_my_schedule,
                headerText = "My Poultry OSO",
                descText = context.getString(R.string.learning_videos_desc)
            ),
            ActivityCardItem(id = "4",
                icon = R.drawable.ic_personal_info,
                headerText = "My ELPM",
                descText = context.getString(R.string.my_homepage_desc)),
            ActivityCardItem(id = "5",
                icon = R.drawable.ic_back,
                headerText = "My Poultry Mother Officer",
                descText = context.getString(R.string.my_homepage_desc)),
            ActivityCardItem(id = "6",
                icon = R.drawable.ic_next,
                headerText = "My Poultry Intern",
                descText = context.getString(R.string.my_homepage_desc)),
        )
    )
    private val entityList: StateFlow<List<ActivityCardItem>> = _entityList.asStateFlow()

    /**
     * Combines two lists of ActivityCardItem into a single StateFlow.
     * This ensures that any updates in either list are reflected in the combined list.
     *
     * - Uses `combine` to merge the latest values from both flows.
     * - Uses `stateIn` to convert it into a `StateFlow`, making it lifecycle-aware.
     * - Uses `SharingStarted.Lazily` to start collecting only when needed.
     * - Provides an initial empty list to prevent null issues.
     */
    val combinedList: StateFlow<List<ActivityCardItem>> = combine(mainList, entityList) { main, entity ->
        main + entity // merge both lists
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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
}
