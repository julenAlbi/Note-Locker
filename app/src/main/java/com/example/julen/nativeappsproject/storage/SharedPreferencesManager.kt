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

    /**
     * Saves the encryption key in the shared preferences
     */
    fun saveEncryptionKey(key: String) {
        shared.edit().putString(STORAGE_ENCRYPTION_KEY, key).apply()
    }

    /**
     * Gets the encryption key from the shared preferences
     */
    fun getEncryptionKey(): String = shared.getString(STORAGE_ENCRYPTION_KEY, "")

    /**
     * @return true if the password is saved ni shared preferences, else false
     */
    fun isPasswordSaved(): Boolean {
        return shared.contains(STORAGE_PASSWORD)
    }

    /**
     * Saves password in shared preferences
     */
    fun savePassword(password: String) {
        shared.edit().putString(STORAGE_PASSWORD, password).apply()
    }

    /**
     * Gets the password from the shared preferences
     */
    fun getPassword(): String = shared.getString(STORAGE_PASSWORD, "")

}