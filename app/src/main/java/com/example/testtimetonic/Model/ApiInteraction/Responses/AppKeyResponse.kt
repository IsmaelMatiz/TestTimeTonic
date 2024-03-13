package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import com.example.testtimetonic.Model.Constants
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

class AppKeyResponse(var context: Context){
    var status: String = ""
    var appkey: String = ""
        set(value) {
            if (value != Constants.RETURN_NULL.constanVal){
                val file = getSecretDocument(KeyName.SECRET_KEY_APPKEY,context)
                val fos = FileOutputStream(file)


                field = CryptographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_APPKEY
                )?.decodeToString().toString()

            }else{
                field = Constants.RETURN_NULL.constanVal
            }
        }
}
