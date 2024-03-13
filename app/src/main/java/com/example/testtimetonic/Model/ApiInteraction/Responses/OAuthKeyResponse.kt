package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import com.example.testtimetonic.Model.Constants
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

/**
 * This class represents a response object containing information retrieved from the OAuth key API.
 *
 * @property context The application context (typically passed from the activity or fragment).
 * @property status The status of the OAuth key retrieval request ("ok" or "nok").
 * @property oauthkey The encrypted OAuth key (if successful) or "null" otherwise.
 * @property oAuthUserid The user ID associated with the OAuth key.
 */
class OAuthKeyResponse (var context: Context) {
    var status: String = ""

    /**
     * The OAuth key retrieved from the API. This is stored in an encrypted format
     * using the provided context and key name.
     */
    var oauthkey: String = ""
        set(value) {
            // Encrypt the oauthkey if it's not null
            if (value != Constants.RETURN_NULL.constanVal){
                val file = getSecretDocument(KeyName.SECRET_KEY_OAUTHKEY,context)
                val fos = FileOutputStream(file)


                field = CryptographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_OAUTHKEY
                )?.decodeToString().toString()// Handle null case
            }else{
                // Set oauthkey to "null"
                field = Constants.RETURN_NULL.constanVal
            }
        }
    var oAuthUserid: String = ""
}