package com.farmbase.app.auth.globalsnackbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class SnackBarViewModel @Inject constructor(): ViewModel() {

//@HiltViewModel
class SnackBarViewModel() : ViewModel() {

    private val _snackbarMessage = MutableLiveData<String>()
    val snackBarMessage: LiveData<String> get() = _snackbarMessage

    fun showSessionSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun showInternetAvailabilitySnackBar(onclick: () -> Unit) {
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