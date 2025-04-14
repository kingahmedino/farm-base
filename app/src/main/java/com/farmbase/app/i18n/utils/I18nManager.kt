package com.farmbase.app.i18n.utils

import AppStrings
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

/**
 * `I18nManager` is a utility class responsible for managing internationalization (i18n)
 * within the Cafitech application. It handles the initialization and switching of locales,
 * leveraging the `i18n4k` library for localization.
 */
class I18nManager {

    companion object {

        /**
         * A [CoroutineScope] used for launching coroutines within the `I18nManager`.
         * It uses [Dispatchers.IO] for I/O-bound operations and [SupervisorJob] to ensure
         * that a failure in one child coroutine does not affect others.
         */
        private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        /**
         * The currently active [Locale]. It is nullable, indicating that no locale
         * might be set initially.
         */
        var currentLocal: Locale? = null

        /**
         * A flag indicating whether the `i18n4k` library has been initialized.
         */
        private var initiated = false

        /**
         * A [mutableStateOf] holding the current [I18n4kConfigImmutable].
         * This configuration is used by the `i18n4k` library to manage locales.
         */
        private var i18n4kConfig = mutableStateOf(I18n4kConfigImmutable())

        /**
         * Initializes the `i18n4k` library and sets up the locale management.
         *
         * This function should be called once during the application's startup.
         * It configures `i18n4k` and starts observing changes to the saved locale
         * in the [LocaleDatastore].
         *
         * @param context The application context, used to access resources and the datastore.
         */
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
                LocaleDatastore(context).getSavedLocal().collectLatest { savedLocale ->
                    val locale = AppStrings.locales.find { locale -> locale.language == savedLocale }
                    locale?.let {
                        setLocale(it, context)
                    }
                }
            }
        }

        /**
         * Sets the current locale and updates the `i18n4k` configuration.
         *
         * This function saves the selected locale to the [LocaleDatastore] and
         * updates the `i18n4kConfig` to reflect the new locale.
         *
         * @param locale The [Locale] to set as the current locale.
         * @param context The application context, used to access the datastore.
         */
        fun setLocale(locale: Locale, context: Context) {
            scope.launch {
                LocaleDatastore(context).saveLocal(locale)
            }
            currentLocal = locale
            i18n4kConfig.value = i18n4kConfig.value.withLocale(locale)
        }
    }
}