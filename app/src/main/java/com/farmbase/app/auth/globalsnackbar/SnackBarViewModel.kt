package com.farmbase.app.auth.globalsnackbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnackBarViewModel @Inject constructor(): ViewModel() {

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

    fun dismissSnackbar() {
        viewModelScope.launch {
            SnackBarController.dismissSnackbar()  // Call the dismiss function
        }
    }

}