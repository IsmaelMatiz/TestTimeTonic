package com.example.testtimetonic.Model.Crythographer

import android.content.Context
import android.util.Log
import com.example.testtimetonic.ModelViews.LoginVM
import java.io.File


fun getSecretDocument(chosenKeyName: KeyName, context: Context): File {

    var file = File(context.filesDir, chosenKeyName.valKeyName+"File.txt")
    if(!file.exists()) {
        file.createNewFile()
    }else{
        file = File(context.filesDir, chosenKeyName.valKeyName+"File.txt")
    }
    return file
}

fun clearFilesAndKeys(context: Context){
    val cryptoManager = CrypthographerManager()

    try {
        cryptoManager.resetKeys()
    }catch (e : Exception){
        Log.e("ErrorIsm","Error al borrar las keys: $e")
    }

    try {
        context.filesDir.walk().forEach {
            if(it.name.contains("secret")) {
                it.delete()
            }
        }
    }catch (e : Exception){
        Log.e("ErrorIsm","Error al borrar los archivos encriptados: $e")
    }
}
