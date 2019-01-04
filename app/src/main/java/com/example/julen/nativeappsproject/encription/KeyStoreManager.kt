package com.example.julen.nativeappsproject.encription

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal

/**
 * This class wraps [KeyStore] class apis with some additional possibilities.
 *
 * @property keyStore there we can store keys
 * @property context the context of the application
 */
class KeyStoreManager(private val context: Context) {

    companion object{
        val KEY_STORE_NAME = "AndroidKeyStore"
    }

    private val keyStore = createAndroidKeyStore()

    /**
     * Creates a keystore instance for AndroidKeyStore provider.
     */
    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KEY_STORE_NAME)
        keyStore.load(null)
        return keyStore
    }

    /**
     * Generates symmetric [KeyProperties.KEY_ALGORITHM_AES] key with default [KeyProperties.BLOCK_MODE_CBC] and
     * [KeyProperties.ENCRYPTION_PADDING_PKCS7] using default provider.
     */
    fun generateDefaultSymmetricKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        return keyGenerator.generateKey()
    }

    /**
     * Creates symmetric [KeyProperties.KEY_ALGORITHM_AES] key with default [KeyProperties.BLOCK_MODE_CBC] and
     * [KeyProperties.ENCRYPTION_PADDING_PKCS7] and saves it to Android Key Store. [KeyGenParameterSpec] is only
     * available for Android API 23+.
     */
    @TargetApi(23)
    fun createAndroidKeyStoreSymmetricKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE_NAME)
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        return keyGenerator.generateKey()
    }

    /**
     * @return symmetric key from Android Key Store or null if any key with given alias exists
     */
    fun getAndroidKeyStoreSymmetricKey(alias: String): SecretKey? = keyStore.getKey(alias, null) as SecretKey?

    /**
     * Creates asymmetric RSA key with default [KeyProperties.BLOCK_MODE_ECB] and
     * [KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1] and saves it to Android Key Store
     */
    fun createAndroidKeyStoreAsymmetricKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA", KEY_STORE_NAME)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initGeneratorWithKeyGenParameterSpec(generator, alias)
        } else {
            initGeneratorWithKeyPairGeneratorSpec(generator, alias)
        }

        return generator.generateKeyPair()
    }

    /**
     * This function uses [KeyGenParameterSpec] to specify key details and is available only from API 23+
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())
    }

    /**
     * This function uses [KeyPairGeneratorSpec] to specify key details, and is available from API 18+, but is deprecated in API 23
     */
    private fun initGeneratorWithKeyPairGeneratorSpec(generator: KeyPairGenerator, alias: String) {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 20)

        val builder = KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(X500Principal("CN=${alias} CA Certificate"))
                .setStartDate(startDate.time)
                .setEndDate(endDate.time)

        generator.initialize(builder.build())
    }

    /**
     * @return asymmetric keypair from Android Key Store or null if any key with given alias exists
     */
    fun getAndroidKeyStoreAsymmetricKeyPair(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    /**
     * Remove key with given [alias] from Android Key Store
     */
    fun removeAndroidKeyStoreKey(alias: String) = keyStore.deleteEntry(alias)

}