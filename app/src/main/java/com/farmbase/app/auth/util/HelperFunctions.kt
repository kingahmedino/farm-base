package com.farmbase.app.auth.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun convertRawStringToString(@StringRes rawString: Int): String {
    val stringText = stringResource(rawString)
    return stringText
}

