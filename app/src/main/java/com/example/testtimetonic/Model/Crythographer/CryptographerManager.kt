package com.example.testtimetonic.Model.Crythographer

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * A class that manages encryption and decryption operations using the Android KeyStore.
 * It provides functions to encrypt and decrypt data, as well as to manage keys securely.
 */
class CryptographerManager {

    /**
     * Android KeyStore instance for secure key storage and retrieval.
     */
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    /**
     * Creates a Cipher instance for encryption using the specified key.
     *
     * @param chosenKey The key to use for encryption.
     * @return The initialized Cipher object.
     */
    private fun encryptCipher(chosenKey: KeyName): Cipher{
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey(chosenKey))
        }
    }

    /**
     * Creates a Cipher instance for decryption using the specified key and initialization vector (IV).
     *
     * @param iv The initialization vector for decryption.
     * @param chosenKey The key to use for decryption.
     * @return The initialized Cipher object.
     */
    private fun getDecryptCipherForIv(iv: ByteArray, chosenKey: KeyName): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(chosenKey), IvParameterSpec(iv))
        }
    }

    /**
     * Retrieves a key from the keystore using the given key name.
     * Creates a new key if it doesn't exist.
     *
     * @param chosenKey The key name to retrieve.
     * @return The SecretKey object.
     */
    private fun getKey(chosenKey: KeyName): SecretKey{
        val existingKey = keyStore.getEntry(chosenKey.valKeyName, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey(chosenKey)
    }

    /**
     * Creates a new SecretKey in the keystore with the specified algorithm and parameters.
     *
     * @param chosenKey The key name to use for the new key.
     * @return The newly generated SecretKey.
     */
    private fun createKey(chosenKey: KeyName): SecretKey{
        return  KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(chosenKey.valKeyName,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    /**
     * Encrypts the provided bytes using the specified key and writes the encrypted data to an file
     *
     * @param bytes The bytes to encrypt.
     * @param outputStream The output stream to write the encrypted data into a file.
     * @param chosenKey The key to use for encryption.
     * @return The encrypted bytes, or null if an error occurs.
     */
    fun encrypt(bytes: ByteArray, outputStream: OutputStream, chosenKey: KeyName): ByteArray? {
        try {
            val cipher = encryptCipher(chosenKey)
            val encryptedBytes = cipher.doFinal(bytes)
            outputStream.use {
                it.write(cipher.iv.size)
                it.write(cipher.iv)
                it.write(encryptedBytes.size)
                it.write(encryptedBytes)
            }
            return encryptedBytes
        }catch (e: Exception){
            Log.e("ErrorIsm","Error al Encriptar: $e")
        }
        return null
    }

    /**
     * Deletes all keys from the keystore.
     */
    fun resetKeys(){
        keyStore.deleteEntry(KeyName.SECRET_KEY_APPKEY.valKeyName)
        keyStore.deleteEntry(KeyName.SECRET_KEY_PASSWORD.valKeyName)
        keyStore.deleteEntry(KeyName.SECRET_KEY_OAUTHKEY.valKeyName)
        keyStore.deleteEntry(KeyName.SECRET_KEY_SESSKEY.valKeyName)
    }

    /**
     * Decrypts encrypted data from an input stream using the specified key.
     *
     * @param inputStream The input stream containing the encrypted data.
     * @param chosenKey The key to use for decryption.
     * @return The decrypted bytes, or null if an error occurs.
     */
    fun decrypt(inputStream: InputStream,chosenKey: KeyName): ByteArray? {
        try {
            return inputStream.use {
                val ivSize = it.read()
                val iv = ByteArray(ivSize)
                it.read(iv)

                val encryptedBytesSize = it.read()
                val encryptedBytes = ByteArray(encryptedBytesSize)
                it.read(encryptedBytes)

                getDecryptCipherForIv(iv,chosenKey).doFinal(encryptedBytes)
            }
        }catch (e: Exception){
            Log.e("ErrorIsm","Error al Desencriptar: $e")
        }
        return null
    }

    companion object{

        /**
         * The encryption algorithm used for keys in the keystore (AES).
         */
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        /**
         * The block mode used for encryption (CBC - Cipher Block Chaining).
         */
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC

        /**
         * The padding scheme used for encryption (PKCS7).
         */
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7

        /**
         * The transformation string combining algorithm, block mode, and padding.
         */
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}

/**
 * All the keynames for store the encrypted keys
 */
enum class KeyName(val valKeyName: String) {
    SECRET_KEY_PASSWORD("secretKeyPassword"),
    SECRET_KEY_APPKEY("secretKeyAppkey"),
    SECRET_KEY_OAUTHKEY("secretKeyOauthkey"),
    SECRET_KEY_SESSKEY("secretKeySesskey")
}