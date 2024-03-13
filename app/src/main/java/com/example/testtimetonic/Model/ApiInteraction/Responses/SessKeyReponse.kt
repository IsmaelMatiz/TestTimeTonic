package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

/**
 * This class represents a response object containing information retrieved, the session key from API.
 *
 * @property context The application context (typically passed from the activity or fragment).
 * @property status The status of the session key retrieval request ("ok" or "nok").
 * @property sesskey The encrypted session key (if successful) or "null" otherwise.
 */
class SessKeyReponse(var context: Context) {
    var status: String = ""

    /**
     * The session key retrieved from the API. This is stored in an encrypted format
     * using the provided context and key name.
     */
    var sesskey: String = ""
        set(value) {
            // Encrypt the sesskey if it's not null
            if (value != "null"){
                val file = getSecretDocument(KeyName.SECRET_KEY_SESSKEY,context)
                val fos = FileOutputStream(file)


                field = CryptographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_SESSKEY
                )?.decodeToString().toString() // Handle null case
            }else{
                // Set sesskey to "null" if the value is null
                field = "null"
            }
        }
}