package com.farmbase.app.auth.globalsnackbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnackBarViewModel @Inject constructor(): ViewModel() {

    private val _snackbarMessage = MutableLiveData<String>()
    val snackBarMessage: LiveData<String> get() = _snackbarMessage

    fun showSessionSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun showSnackbar() {
        viewModelScope.launch {
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "Internet Connection Lost",
                    action = SnackbarAction(
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

    fun dismissSnackbar() {
        viewModelScope.launch {
            SnackbarController.dismissSnackbar()  // Call the dismiss function
        }
    }

}