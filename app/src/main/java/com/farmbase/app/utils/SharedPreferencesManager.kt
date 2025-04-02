package com.farmbase.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson


class SharedPreferencesManager(private val context: Context) {

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs by lazy {
        context.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE,
        )
    }

    private val encryptedSharedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "ENCRYPTED_SHARED_PREF",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun put(key: String, value: String) {
        with(sharedPrefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun get(key: String, default: String? = null): String? {
        return sharedPrefs.getString(key, default)
    }

    fun remove(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }

    fun encryptedPut(key: String, value: String) {
        with(encryptedSharedPrefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun encryptedGet(key: String, default: String? = null): String? {
        return encryptedSharedPrefs.getString(key, default)
    }

    fun encryptedRemove(key: String) {
        encryptedSharedPrefs.edit().remove(key).apply()
    }

    fun put(key: String, value: Boolean) {
        with(sharedPrefs.edit()) {
            putBoolean(key, value)
            commit()
        }
    }

    fun get(key: String, default: Boolean = false): Boolean {
        return sharedPrefs.getBoolean(key, default)
    }

    inline fun <reified T> putObject(key: String, value: T) {
        put(key, Gson().toJson(value))
    }

    inline fun <reified T> getObject(key: String): T? {
        val raw = get(key, default = null) ?: return null
        return Gson().fromJson(raw, T::class.java)
    }

    inline fun <reified T> encryptedPutObject(key: String, value: T) {
        encryptedPut(key, Gson().toJson(value))
    }

    inline fun <reified T> encryptedGetObject(key: String): T? {
        val raw = encryptedGet(key, default = null) ?: return null
        return Gson().fromJson(raw, T::class.java)
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
    }
}