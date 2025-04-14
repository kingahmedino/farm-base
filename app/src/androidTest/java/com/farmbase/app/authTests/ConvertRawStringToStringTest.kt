package com.farmbase.app.authTests

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createComposeRule
import com.farmbase.app.R
import com.farmbase.app.auth.util.convertRawStringToString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalComposeUiApi::class)
class ConvertRawStringToStringTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testConvertRawStringToString_returnsCorrectString() {
        lateinit var result: String

        composeTestRule.setContent {
            result = convertRawStringToString(R.string.hello_world)
        }

        // Replace "Hello, World!" with the actual string in your `res/values/strings.xml`
        assertEquals("Hello, World!", result)
    }
}
