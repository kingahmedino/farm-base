package com.farmbase.app.i18n.utils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.farmbase.app.auth.datastore.repo.LocaleDatastore
import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDelegate
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.i18n4k
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import x.y.MyMessages

class I18nManager {

    companion object {

        private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        var currentLocal: Locale? = null

        private var initiated = false

        private var i18n4kConfig = mutableStateOf(I18n4kConfigImmutable())

        fun initI18n4k(context: Context) {
            if (initiated) return

            i18n4k = I18n4kConfigDelegate { i18n4kConfig.value }

            /* // load "fr" at runtime
             MyMessages.registerTranslation(
                 MessagesProviderViaText(
                     text = String(
                         context.resources.openRawResource(
                             R.raw.x_y_my_messages_fr_i18n4k
                         ).readBytes()
                     )
                 )
             )*/

            initiated = true

            scope.launch {
                LocaleDatastore(context).getSavedLocal().collectLatest {
                    val local = MyMessages.locales.find { locale -> locale.language == it }
                    local?.let {
                        setLocale(it, context)
                    }
                }
            }
        }

        fun setLocale(locale: Locale, context: Context) {
            scope.launch {
                LocaleDatastore(context).saveLocal(locale)
            }
            currentLocal = locale
            i18n4kConfig.value = i18n4kConfig.value.withLocale(locale)
        }
    }
}