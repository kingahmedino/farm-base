package com.farmbase.app.auth.util

import android.content.Context
import android.net.Uri
import android.widget.Button
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.farmbase.app.R

object AuthObjects {

    fun launchWebsite(context: Context){
        val URL = "https://iam-service-frontend-v25.agric-os.com/"

        val customizeChromeIntent = CustomTabsIntent.Builder()
            // set Color of the Toolbar
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder().setToolbarColor(ContextCompat.getColor(context, R.color.cafitech_light_green))
                    .build()
            )
            // set Dark mode theme
            .setColorSchemeParams(
                CustomTabsIntent.COLOR_SCHEME_DARK, CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(context, R.color.cafitech_dark_green))
                    .build()
            )
            // Auto Hide App Bar
            .setUrlBarHidingEnabled(true)
            // Show Title
            .setShowTitle(true)
            .build()

            customizeChromeIntent.launchUrl(context, Uri.parse(URL))
    }
}