package com.example.testtimetonic.Model.ApiInteraction.Responses

import android.content.Context
import android.util.Log
import com.example.testtimetonic.Model.Crythographer.CrypthographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import java.io.FileOutputStream

class AppKeyResponse(var context: Context){
    var status: String = ""
    var appkey: String = ""
        set(value) {
            if (value != "null"){
                val file = getSecretDocument(KeyName.SECRET_KEY_APPKEY,context)
                val fos = FileOutputStream(file)

                Log.i("IsmDebug","Empiezo la conversion")

                field = CrypthographerManager().encrypt(
                    bytes = value.encodeToByteArray(),
                    outputStream = fos,
                    KeyName.SECRET_KEY_APPKEY
                )?.decodeToString().toString()
            }else{
                field = "null"
            }
        }
}
