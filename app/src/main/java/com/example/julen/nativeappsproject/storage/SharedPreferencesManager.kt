package com.example.julen.nativeappsproject.storage

import android.content.Context
import android.content.SharedPreferences

/**
 * This class will manage the data from shared preferences.
 */

class SharedPreferencesManager(context: Context) {

    private val shared: SharedPreferences

    companion object {
        private val STORAGE_SETTINGS: String = "sharedPreferences"
        private val STORAGE_ENCRYPTION_KEY: String = "encryptionKey"
        private val STORAGE_PASSWORD: String = "password"
    }

    init {
        shared = context.getSharedPreferences(STORAGE_SETTINGS, android.content.Context.MODE_PRIVATE)
    }

    fun saveEncryptionKey(key: String) {
        shared.edit().putString(STORAGE_ENCRYPTION_KEY, key).apply()
    }

    fun getEncryptionKey(): String = shared.getString(STORAGE_ENCRYPTION_KEY, "")

    fun isPasswordSaved(): Boolean {
        return shared.contains(STORAGE_PASSWORD)
    }

    fun savePassword(password: String) {
        shared.edit().putString(STORAGE_PASSWORD, password).apply()
    }

    fun getPassword(): String = shared.getString(STORAGE_PASSWORD, "")

}