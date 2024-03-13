package com.example.testtimetonic.ModelViews

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtimetonic.Model.ApiInteraction.Responses.AppKeyResponse
import com.example.testtimetonic.Model.ApiInteraction.Responses.OAuthKeyResponse
import com.example.testtimetonic.Model.ApiInteraction.RetroFitClient
import com.example.testtimetonic.Model.Constants
import com.example.testtimetonic.Model.Crythographer.CryptographerManager
import com.example.testtimetonic.Model.Crythographer.KeyName
import com.example.testtimetonic.Model.Crythographer.getSecretDocument
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.FileInputStream
import java.io.FileOutputStream

class LoginVM(application: Application): AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled : LiveData<Boolean> = _loginEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isPasswordVisible = MutableLiveData<Boolean>(false)
    val isPasswordVisible : LiveData<Boolean> = _isPasswordVisible

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean  = password.length > 6


    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun onPasswordVisibleSelected(){
        _isPasswordVisible.value = !_isPasswordVisible.value!!
    }

    suspend fun onLoginSelected(navToLanding:() -> Unit): Int {
        _isLoading.value = true

        var errorsAlongTheProcess = 0
        encryptPassword(password?.value?:"")

        //TODO implement the logic for login with the API calls

        viewModelScope.launch { errorsAlongTheProcess += authStep1() }.join()
        viewModelScope.launch { errorsAlongTheProcess += authStep2(email?.value.toString())}.join()

        Log.i("IsmInfo","Errors along Auth process $errorsAlongTheProcess errors")
        if (errorsAlongTheProcess == 0)
        {
            navToLanding()
            _isLoading.value = false
            return 0
        }
        else {
            _isLoading.value = false
            return 1
        }
    }

    /**
     * This suspend function performs step 1 of the authentication process.
     * It retrieves an app key from a web service and stores it in a file if the response
     * is successful.
     *
     * @throws Exception if there is an error during the network call, JSON parsing,
     * or storing the app key.
     *
     * @return 0 on success, 1 on failure.
     */
    suspend fun authStep1(): Int{
        try {
            // 1. Fetch app key from web service
            val appKeyResponse = AppKeyResponse(context)
            val response = RetroFitClient(context).webServiceLogin
                .createAppKey("createAppkey","api")

            // 2. Parse the JSON response
            val jsonOb = Json.parseToJsonElement(response.body().toString()).jsonObject

            // 3. Extract status and handle success/failure and set the app key
            appKeyResponse.status = jsonOb["status"]?.toString()
                ?.replace("\"","") ?: "nok"
            if (appKeyResponse.status == "ok")
            {
                appKeyResponse.appkey = jsonOb["appkey"]?.toString()
                    ?.replace("\"","") ?: "empty"
            }
            else {
                appKeyResponse.appkey = "null"
                return 1// Indicate failure
            }

        }catch (e : Exception){
            Log.e("BookStoreDebug", "An error occur along firt auth step:\n$e")
            return 1 // Indicate failure
        }

        return 0 // Indicate success
    }

    suspend fun authStep2(
        login: String
    ): Int{
        //try {
            // 1. Fetch Oauth key  from web service
            val oAuthKeyResponse = OAuthKeyResponse(context)
            val response = RetroFitClient(context).webServiceLogin
                .createOauthkey(
                    Constants.CREATE_OAUTHKEY.constanVal,
                    login,
                    getPassword(),
                    getAppKey())

            // 2. Parse the JSON response
            val jsonOb = Json.parseToJsonElement(response.body().toString()).jsonObject

            // 3. Extract status and handle success/failure and set the app key
            oAuthKeyResponse.status = jsonOb["status"]?.toString()
                ?.replace("\"","") ?: "nok"
            if (oAuthKeyResponse.status == "ok")
            {
                oAuthKeyResponse.oauthkey = jsonOb["appkey"]?.toString()
                    ?.replace("\"","") ?: "empty"
            }
            else {
                oAuthKeyResponse.oauthkey = "null"
                return 1// Indicate failure
            }

       // }catch (e : Exception){
       //     Log.e("BookStoreDebug", "An error occur along second auth step:\n$e")
       //     return 1 // Indicate failure
        //}

        return 0 // Indicate success
    }

    private fun getAppKey(): String{
        val file = getSecretDocument(KeyName.SECRET_KEY_APPKEY,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_APPKEY
        )?.decodeToString().toString()
    }

    private fun getPassword(): String{
        val file = getSecretDocument(KeyName.SECRET_KEY_PASSWORD,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_PASSWORD
        )?.decodeToString().toString()
    }

    private fun encryptPassword(password: String){
        val file = getSecretDocument(KeyName.SECRET_KEY_PASSWORD,context)
        val fos = FileOutputStream(file)

        CryptographerManager().encrypt(
            bytes = password.encodeToByteArray(),
            outputStream = fos,
            KeyName.SECRET_KEY_PASSWORD
        )?.decodeToString().toString()
    }
}

