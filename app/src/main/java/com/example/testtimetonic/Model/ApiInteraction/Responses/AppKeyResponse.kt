package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import com.example.testtimetonic.Model.Constants
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

/**
 * This class represents a response object containing information retrieved from the app key API.
 *
 * @property context The application context (typically passed from the activity or fragment).
 * @property status The status of the app key retrieval request ("ok" or "nok").
 * @property appkey The encrypted app key (if successful) or "null" otherwise.
 */
class AppKeyResponse(var context: Context){
    var status: String = ""
    /**
     * The app key retrieved from the API. This is encrypted and store in a file
     */
    var appkey: String = ""
        set(value) {
            // Encrypt the app key if it's not null
            if (value != Constants.RETURN_NULL.constanVal){
                val file = getSecretDocument(KeyName.SECRET_KEY_APPKEY,context)
                val fos = FileOutputStream(file)


                field = CryptographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_APPKEY
                )?.decodeToString().toString()// Handle null case

            }else{
                // Set appkey to "null"
                field = Constants.RETURN_NULL.constanVal
            }
        }
}
