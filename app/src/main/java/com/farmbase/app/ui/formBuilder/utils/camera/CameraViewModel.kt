package com.farmbase.app.ui.formBuilder.utils.camera

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    private val _permissionsGranted = mutableStateOf(false)
    val permissionsGranted: State<Boolean> = _permissionsGranted

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted) {
            _permissionsGranted.value = false
        } else {
            _permissionsGranted.value = true
        }
    }
}
