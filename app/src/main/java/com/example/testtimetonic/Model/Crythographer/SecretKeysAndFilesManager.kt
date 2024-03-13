package com.example.testtimetonic.Model.Crythographer

import android.content.Context
import android.util.Log
import java.io.File

/**
 * Retrieves or creates a file for storing encrypted credentials based on the specified key name.
 *
 * @param chosenKeyName The enum value representing the type of credential to store (e.g., SECRET_KEY_PASSWORD).
 * @param context The application context required to access the files directory.
 * @return A File object representing the file for the chosen credential.
 */
fun getSecretDocument(chosenKeyName: KeyName, context: Context): File {

    // Combine key name with "File.txt"
    var file = File(context.filesDir, chosenKeyName.valKeyName+"File.txt")
    if(!file.exists()) {
        file.createNewFile() // Create a new file if it doesn't exist
    }

    return file
}

/**
 * Clears all encrypted credential files and resets encryption keys.
 *
 * @param context The application context required to access the files directory.
 */
fun clearFilesAndKeys(context: Context){
    val cryptoManager = CryptographerManager()

    try {
        cryptoManager.resetKeys() // Delete all keys from the keystore
    }catch (e : Exception){
        Log.e("ErrorIsm","Error al borrar las keys: $e")
    }

    try {
        context.filesDir.walk().forEach {
            if(it.name.contains("secret")) {// Delete files containing "secret" in the name
                it.delete()
            }
        }
    }catch (e : Exception){
        Log.e("ErrorIsm","Error al borrar los archivos encriptados: $e")
    }
}
