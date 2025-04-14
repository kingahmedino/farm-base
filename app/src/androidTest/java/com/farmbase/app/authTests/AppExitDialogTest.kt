package com.farmbase.app.authTests

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.farmbase.app.R
import com.farmbase.app.auth.util.AppExitDialog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class AppExitDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun exitDialog_showsOnBackPress_andRespondsToClick() {
        val activity = composeTestRule.activity

        composeTestRule.setContent {
            AppExitDialog(activity)
        }

        // Simulate back press
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }

        // Verify dialog is displayed
        composeTestRule.onNodeWithText(
            activity.getString(R.string.are_you_sure_you_want_to_close_the_app)
        ).assertIsDisplayed()

        // Click the "No" button to dismiss
        composeTestRule.onNodeWithText(activity.getString(R.string.no))
            .assertIsDisplayed()
            .performClick()

        // Verify dialog is dismissed
        composeTestRule.onNodeWithText(
            activity.getString(R.string.are_you_sure_you_want_to_close_the_app)
        ).assertDoesNotExist()
    }
}
