package com.example.julen.nativeappsproject.encription

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import android.annotation.TargetApi
import java.security.KeyPair
import java.security.KeyPairGenerator
import android.os.Build
import android.security.KeyPairGeneratorSpec
import java.math.BigInteger
import java.security.PrivateKey
import java.util.*
import javax.security.auth.x500.X500Principal

class KeyStoreManager(private val context: Context) {

    companion object{
        val KEY_STORE_NAME = "AndroidKeyStore"
    }

    private val keyStore = createAndroidKeyStore()


    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KEY_STORE_NAME)
        keyStore.load(null)
        return keyStore
    }

    fun createMasterKey(master_key: String){

    }

    fun generateDefaultSymmetricKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        return keyGenerator.generateKey()
    }

    @TargetApi(23)
    fun createAndroidKeyStoreSymmetricKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        return keyGenerator.generateKey()
    }

    fun getAndroidKeyStoreSymmetricKey(alias: String): SecretKey? = keyStore.getKey(alias, null) as SecretKey?

    fun createAndroidKeyStoreAsymmetricKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initGeneratorWithKeyGenParameterSpec(generator, alias)
        } else {
            initGeneratorWithKeyPairGeneratorSpec(generator, alias)
        }

        return generator.generateKeyPair()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())
    }
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

    fun getAndroidKeyStoreAsymmetricKeyPair(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    fun removeAndroidKeyStoreKey(alias: String) = keyStore.deleteEntry(alias)

}