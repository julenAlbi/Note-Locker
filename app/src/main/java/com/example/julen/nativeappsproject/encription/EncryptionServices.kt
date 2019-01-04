package com.example.julen.nativeappsproject.encription

import android.content.Context
import android.os.Build
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * This class uses [CipherManager] and [KeyStoreManager] to provide methods to encrypt and decrypt data.
 *
 * It only conatins four public methods, [createMasterKey], [removeMasterKey], [encrypt] and [decrypt].
 * The application uses this methods to make the cypher.
 * @property keyStoreManager will be used to save and retrieve keys from androidKeyStore or default java provider.
 * @property storage is used to store a symmetric key, in case that the android api is lower than 23 and higher
 * than 18. We need a symmetric key to encrypt data, because with a asymmetric key we can't encrypt large data.
 * See comments of [createMasterKey] to understand the process.
 */
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
     * Create and save cryptography key.
     *
     * Creation of the cryptography key depends on the android version. If Android 23+ API, it will create a symmetric key in
     * androidKeyStore, else it will create android a asymmetric key in the androidKeyStore, and with that key it will encrypt
     * another symmetric key created with default java provider.
     */
    fun createMasterKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createAndroidSymmetricKey()
        }else{
            createDefaultSymmetricKey()
        }
    }

    /**
     * Remove master cryptography key. May be used for re sign up functionality.
     */
    fun removeMasterKey() {
        keyStoreManager.removeAndroidKeyStoreKey(MASTER_KEY)
    }

    /**
     * Creates a symmetric key with android key store
     */
    private fun createAndroidSymmetricKey() {
        keyStoreManager.createAndroidKeyStoreSymmetricKey(MASTER_KEY)
    }

    /**
     * Creates a symmetric key with default java provider
     */
    private fun createDefaultSymmetricKey() {
        val symmetricKey = keyStoreManager.generateDefaultSymmetricKey()
        val masterKey = keyStoreManager.createAndroidKeyStoreAsymmetricKey(MASTER_KEY)
        val encryptedSymmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).wrapKey(symmetricKey, masterKey.public)
        storage.saveEncryptionKey(encryptedSymmetricKey)
    }

    /**
     * Encrypt user password and Secrets with created master key.
     */
    fun encrypt(data: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptWithAndroidSymmetricKey(data)
        } else {
            encryptWithDefaultSymmetricKey(data)
        }
    }

    /**
     * Decrypt user password and Secrets with created master key.
     */
    fun decrypt(data: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decryptWithAndroidSymmetricKey(data)
        } else {
            decryptWithDefaultSymmetricKey(data)
        }
    }

    /**
     * Encrypts [data] with a symmetric key provided by android key store
     */
    private fun encryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey, true)
    }

    /**
     * Decrypts [data] with a symmetric key provided by android key store
     */
    private fun decryptWithAndroidSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreSymmetricKey(MASTER_KEY)
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey, true)
    }

    /**
     * Encrypts [data] with a symmetric key provided by default java provider
     */
    private fun encryptWithDefaultSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val encryptionKey = storage.getEncryptionKey()
        val symmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptionKey, ALGORITHM_AES, Cipher.SECRET_KEY, masterKey?.private) as SecretKey
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).encrypt(data, symmetricKey, true)
    }

    /**
     * Encrypts [data] with a symmetric key provided by default java provider
     */
    private fun decryptWithDefaultSymmetricKey(data: String): String {
        val masterKey = keyStoreManager.getAndroidKeyStoreAsymmetricKeyPair(MASTER_KEY)
        val encryptionKey = storage.getEncryptionKey()
        val symmetricKey = CipherManager(CipherManager.TRANSFORMATION_ASYMMETRIC).unWrapKey(encryptionKey, ALGORITHM_AES, Cipher.SECRET_KEY, masterKey?.private) as SecretKey
        return CipherManager(CipherManager.TRANSFORMATION_SYMMETRIC).decrypt(data, symmetricKey, true)
    }
}