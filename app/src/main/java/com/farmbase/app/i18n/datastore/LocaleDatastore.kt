package com.farmbase.app.auth.datastore.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.comahe.i18n4k.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.localDatastore: DataStore<Preferences> by preferencesDataStore(name = "Locale_DataStore")


class LocaleDatastore(private val context: Context) {

    companion object {
        val CURRENT_LOCAL = stringPreferencesKey("CURRENT_LOCAL")

    }

    suspend fun saveLocal(locale: Locale) {
        context.localDatastore.edit { prefs ->
            prefs[CURRENT_LOCAL] = locale.language

        }
    }

    fun getSavedLocal(): Flow<String> =
        context.localDatastore.data.map { prefs ->
            prefs[CURRENT_LOCAL].toString()
        }

}

