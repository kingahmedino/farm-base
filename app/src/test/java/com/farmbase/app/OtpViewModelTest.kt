package com.farmbase.app

import com.farmbase.app.auth.ui.components.otp.OtpAction
import com.farmbase.app.auth.ui.components.otp.OtpViewModel
import com.farmbase.app.utils.HashHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OtpViewModelTest {

    private lateinit var viewModel: OtpViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = OtpViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onChangeFieldFocused updates focusedIndex`() = runTest {
        viewModel.onAction(OtpAction.OnChangeFieldFocused(2))
        assertEquals(2, viewModel.state.value.focusedIndex)
    }

    @Test
    fun `onEnterNumber updates code at index`() = runTest {
        viewModel.onAction(OtpAction.OnEnterNumber(5, 1))
        assertEquals(listOf(null, 5, null, null), viewModel.state.value.code)
    }

    @Test
    fun `onEnterNumber with full code and correct hash sets isValid true`() = runTest {
        val otp = listOf(1, 2, 3, 4)
        val otpString = otp.joinToString("")
        viewModel.firstOtpCodeData = HashHelper.sha256(otpString)

        otp.forEachIndexed { index, digit ->
            viewModel.onAction(OtpAction.OnEnterNumber(digit, index))
        }

        assertEquals(true, viewModel.state.value.isValid)
    }

    @Test
    fun `onEnterNumber with incorrect OTP sets isValid false`() = runTest {
        viewModel.firstOtpCodeData = HashHelper.sha256("1234")

        viewModel.onAction(OtpAction.OnEnterNumber(1, 0))
        viewModel.onAction(OtpAction.OnEnterNumber(2, 1))
        viewModel.onAction(OtpAction.OnEnterNumber(3, 2))
        viewModel.onAction(OtpAction.OnEnterNumber(9, 3)) // wrong digit

        assertEquals(false, viewModel.state.value.isValid)
    }

    @Test
    fun `onKeyboardBack removes previous digit and updates focus`() = runTest {
        viewModel.onAction(OtpAction.OnEnterNumber(1, 0))
        viewModel.onAction(OtpAction.OnChangeFieldFocused(1))
        viewModel.onAction(OtpAction.OnKeyboardBack)

        assertEquals(listOf(null, null, null, null), viewModel.state.value.code)
        assertEquals(0, viewModel.state.value.focusedIndex)
    }
}
