package com.example.julen.nativeappsproject.encription

import android.content.Context
import android.os.Build
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager
import javax.crypto.Cipher
import javax.crypto.SecretKey

class EncryptionServices(private val context: Context) {
    /**
     * The place to keep all constants.
     */
    companion object {
        val MASTER_KEY = "MASTER_KEY"

        val ALGORITHM_AES = "AES"
    }

    private val keyStoreManager = KeyStoreManager(context)
    private val storage = SharedPreferencesManager(context)

    /**
     * Creates a master key depending on the android version. If Android 23+ API, ti will create a symmetric key in
     * androidKeyStore, else it will create android a asymmetric key in the androidKeyStoer, and with that key it will encrypt
     * another symmetric key created with defalut java provider.
     */
    fun createMasterKey() {
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

    fun encrypt(data: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptWithAndroidSymmetricKey(data)
        } else {
            encryptWithDefaultSymmetricKey(data)
        }
    }

    fun decrypt(data: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decryptWithAndroidSymmetricKey(data)
        } else {
            decryptWithDefaultSymmetricKey(data)
        }
    }

    private fun encryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey, true)
    }

    private fun decryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey, true)
    }

    private fun encryptWithDefaultSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val encryptionKey = storage.getEncryptionKey()
        val symmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptionKey, ALGORITHM_AES, Cipher.SECRET_KEY, masterKey?.private) as SecretKey
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).encrypt(data, symmetricKey, true)
    }

    private fun decryptWithDefaultSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val encryptionKey = storage.getEncryptionKey()
        val symmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptionKey, ALGORITHM_AES, Cipher.SECRET_KEY, masterKey?.private) as SecretKey
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).decrypt(data, symmetricKey, true)
    }
}