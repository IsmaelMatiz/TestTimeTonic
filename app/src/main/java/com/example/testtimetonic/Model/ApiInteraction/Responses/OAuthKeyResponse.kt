package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

class OAuthKeyResponse (var context: Context) {
    var status: String = ""
    var oauthkey: String = ""
        set(value) {
            if (value != "null"){
                val file = getSecretDocument(KeyName.SECRET_KEY_OAUTHKEY,context)
                val fos = FileOutputStream(file)


                field = CryptographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_OAUTHKEY
                )?.decodeToString().toString()
            }else{
                field = "null"
            }
        }
    var oAuthUserid: String = ""
}