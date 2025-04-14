package com.farmbase.app.auth.globalsnackbar

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmbase.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnackBarViewModel @Inject constructor(): ViewModel() {

    fun showSnackBar() {
        viewModelScope.launch {
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "Internet Connection Lost",
                    action = SnackBarAction(
                        name = "Okay!",
                        action = {
                            dismissSnackbar()
                            // SnackbarController.dismissSnackbar()
                        }
                    )
                )
            )
        }
    }

    fun showSnackbar() {
        viewModelScope.launch {
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "Internet Connection Lost",
                    action = SnackBarAction(
                        name = "Okay!",
                        action = {
                            dismissSnackbar()
                        }
                    )
                )
            )
        }
    }

    fun showAppSnackBar(message: String, buttonMessage: String) {
        viewModelScope.launch {
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = message,
                    action = SnackBarAction(
                        name = buttonMessage,
                        action = {
                            dismissSnackbar()
                        }
                    )
                )
            )
        }
    }

    fun showSnackBarMessage(message: String, buttonText: String) {
        viewModelScope.launch {
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = message,
                    action = SnackBarAction(
                        name = buttonText,
                        action = {
                            dismissSnackbar()
                        }
                    )
                )
            )
        }
    }

    fun dismissSnackbar() {
        viewModelScope.launch {
            SnackBarController.dismissSnackbar()  // Call the dismiss function
        }
    }

}