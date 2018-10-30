package com.example.julen.nativeappsproject.encription

import android.content.Context
import android.os.Build
import javax.crypto.Cipher
import javax.crypto.SecretKey

class EncriptionServices(private val context: Context) {
    /**
     * The place to keep all constants.
     */
    companion object {
        val MASTER_KEY = "MASTER_KEY"

        val ALGORITHM_AES = "AES"
    }

    private val keyStoreManager = KeyStoreManager(context)
    private val storage = SharedPreferencesManager(context)

    fun createAppPassword(password: String) {
        /*
        Create key for encript password.
        first encript password and store it in the sahredpreferences.
         */
        createMasterKey()

    }

    fun createMasterKey(keyPassword: String? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createAndroidSymmetricKey()
        }else{
            createDefaultSymmetricKey()
        }
    }

    fun removeMasterKey() {
        keyStoreManager.removeAndroidKeyStoreKey(MASTER_KEY)
    }

    private fun createAndroidSymmetricKey() {
        keyStoreManager.createAndroidKeyStoreSymmetricKey(MASTER_KEY)
    }

    private fun createDefaultSymmetricKey() {
        val symmetricKey = keyStoreManager.generateDefaultSymmetricKey()
        val masterKey = keyStoreManager.createAndroidKeyStoreAsymmetricKey(MASTER_KEY)
        val encryptedSymmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).wrapKey(symmetricKey, masterKey.public)
        storage.saveEncryptionKey(encryptedSymmetricKey)
    }

    fun encrypt(data: String, keyPassword: String? = null): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptWithAndroidSymmetricKey(data)
        } else {
            encryptWithDefaultSymmetricKey(data)
        }
    }

    fun decrypt(data: String, keyPassword: String? = null): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decryptWithAndroidSymmetricKey(data)
        } else {
            decryptWithDefaultSymmetricKey(data)
        }
    }

    private fun encryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey)
    }

    private fun decryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey)
    }

    private fun encryptWithDefaultSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val encryptionKey = storage.getEncryptionKey()
        val symmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptionKey, ALGORITHM_AES, Cipher.SECRET_KEY, masterKey?.private) as SecretKey
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).encrypt(data, symmetricKey)
    }

    private fun decryptWithDefaultSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val encryptionKey = storage.getEncryptionKey()
        val symmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptionKey, ALGORITHM_AES, Cipher.SECRET_KEY, masterKey?.private) as SecretKey
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).decrypt(data, symmetricKey)
    }
}