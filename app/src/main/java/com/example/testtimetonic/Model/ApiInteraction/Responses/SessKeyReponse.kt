package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

class SessKeyReponse(var context: Context) {
    var status: String = ""
    var sesskey: String = ""
        set(value) {
            if (value != "null"){
                val file = getSecretDocument(KeyName.SECRET_KEY_SESSKEY,context)
                val fos = FileOutputStream(file)


                field = CryptographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_SESSKEY
                )?.decodeToString().toString()
            }else{
                field = "null"
            }
        }
}