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
import com.example.testtimetonic.Model.ApiInteraction.Responses.SessKeyReponse
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

/**
 * ViewModel class responsible for handling login logic and user interaction within the login screen.
 *
 * @property context The application context obtained from the application instance.
 *
 * **LiveData properties:**
 *  - _email: Stores the entered email address.
 *  - _password: Stores the entered password (on login selected is encrypted).
 *  - _loginEnabled: Indicates whether the login button should be enabled based on email and password validity.
 *  - _isLoading: Indicates if the login process is currently ongoing.
 *  - _isPasswordVisible: Indicates if the password field should display plain text or hidden characters.
 */
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

    /**
     * Updates the internal state of the email and password based on user input,
     * and sets the login button enabled/disabled based on validity checks.
     *
     * @param email The entered email address.
     * @param password The entered password.
     */
    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }

    /**
     * Simple password length validation.
     *
     * @param password The password to validate.
     * @return True if the password is at least 6 characters long, false otherwise.
     */
    private fun isValidPassword(password: String): Boolean  = password.length > 6

    /**
     * Uses the Android Patterns class to validate the email format(regex under the hood).
     *
     * @param email The email address to validate.
     * @return True if the email format is valid, false otherwise.
     */
    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Toggles the visibility state of the password field between plain text and hidden characters.
     */
    fun onPasswordVisibleSelected() {_isPasswordVisible.value = !_isPasswordVisible.value!!}

    /**
     * Coroutine function that handles the entire login process.
     * It performs the following steps:
     *  1. Fetches and stores the app key (if successful).
     *  2. Fetches and stores the OAuth key (if successful).
     *  3. Fetches and stores the session key (if successful).
     *  4. Logs the number of errors encountered during the process.
     *  5. Navigates to the landing screen if no errors occurred, otherwise displays an error message.
     *
     * @param navToLanding A lambda function to navigate to the landing screen.
     * @return 0 on success, 1 or more on any errors encountered.
     */
    suspend fun onLoginSelected(navToLanding:() -> Unit): Int {
        _isLoading.value = true

        var errorsAlongTheProcess = 0
        val oAuthKeyResponse = OAuthKeyResponse(context)
        encryptPassword(password?.value?:"")

        // Launch separate coroutines for each auth step to potentially improve performance
        viewModelScope.launch { errorsAlongTheProcess += authStep1() }.join()
        viewModelScope.launch { errorsAlongTheProcess += authStep2(
            email?.value.toString(),
            oAuthKeyResponse
        )}.join()
        viewModelScope.launch {
            errorsAlongTheProcess += authStep3(oAuthKeyResponse.oAuthUserid) }.join()


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
            val response = RetroFitClient().webServiceLogin
                .createAppKey(
                    Constants.CREATE_APPKEY.constanVal,
                    Constants.APP_NAME_APICALL.constanVal)

            // 2. Parse the JSON response
            val jsonOb = Json.parseToJsonElement(response.body().toString()).jsonObject

            // 3. Extract status and handle success/failure and set the app key
            appKeyResponse.status = jsonOb[Constants.STATUS.constanVal]?.toString()
                ?.replace("\"","") ?: Constants.STATUS_NOT_OK.constanVal
            if (appKeyResponse.status == Constants.STATUS_OK.constanVal)
            {
                appKeyResponse.appkey = jsonOb[Constants.REF_JSON_OBJ_APPKEY.constanVal]?.toString()
                    ?.replace("\"","") ?: Constants.RETURN_EMPTY.constanVal
            }
            else {
                appKeyResponse.appkey = Constants.RETURN_NULL.constanVal
                return 1// Indicate failure
            }

        }catch (e : Exception){
            Log.e("BookStoreDebug", "An error occur along firt auth step:\n$e")
            return 1 // Indicate failure
        }

        return 0 // Indicate success
    }

    /**
     * This suspend function performs step 2 of the authentication process.
     * It retrieves an OAuth key from a web service using the provided login credentials
     * and the previously retrieved app key.
     *
     * @param login The user's login credential (email address).
     * @param oAuthKeyResponse An object to store the retrieved OAuth key.
     * @return 0 on success, 1 on any errors encountered.
     */
    suspend fun authStep2(
        login: String,
        oAuthKeyResponse: OAuthKeyResponse
    ): Int{
        try {
            // 1. Fetch Oauth key  from web service
            val response = RetroFitClient().webServiceLogin
                .createOauthkey(
                    Constants.CREATE_OAUTHKEY.constanVal,
                    login,
                    getPassword(),
                    getAppKey())

            // 2. Parse the JSON response
            val jsonOb = Json.parseToJsonElement(response.body().toString()).jsonObject

            // 3. Extract status and handle success/failure and set the Oauth key
            oAuthKeyResponse.status = jsonOb[Constants.STATUS.constanVal]?.toString()
                ?.replace("\"","") ?: Constants.STATUS_NOT_OK.constanVal
            if (oAuthKeyResponse.status == Constants.STATUS_OK.constanVal)
            {
                oAuthKeyResponse.oauthkey = jsonOb[Constants.REF_JSON_OBJ_OAUTHKEY.constanVal]?.toString()
                    ?.replace("\"","") ?: Constants.RETURN_EMPTY.constanVal

                oAuthKeyResponse.oAuthUserid = jsonOb[Constants.REF_JSON_OBJ_OAUTH_ID.constanVal]?.toString()
                    ?.replace("\"","") ?: Constants.RETURN_EMPTY.constanVal
            }
            else {
                oAuthKeyResponse.oauthkey = Constants.RETURN_NULL.constanVal
                return 1// Indicate failure
            }

       }catch (e : Exception){
            Log.e("BookStoreDebug", "An error occur along second auth step:\n$e")
            return 1 // Indicate failure
       }

        return 0 // Indicate success
    }

    /**
     * This suspend function performs step 3 of the authentication process.
     * It retrieves a session key from a web service using the provided OAuth user ID
     * and the previously retrieved OAuth key.
     *
     * @param oAuthUserid The user ID obtained from the OAuth response.
     * @return 0 on success, 1 on any errors encountered.
     */
    suspend fun authStep3(
        oAuthUserid: String,
    ): Int{
        try {
            // 1. Fetch Sess key  from web service
            val sessKeyResponse = SessKeyReponse(context)
            val response = RetroFitClient().webServiceLogin
                .createSessKey(
                    Constants.CREATE_SESSKEY.constanVal,
                    oAuthUserid,
                    getOAuthKey()
                )

            // 2. Parse the JSON response
            val jsonOb = Json.parseToJsonElement(response.body().toString()).jsonObject

            // 3. Extract status and handle success/failure and set the Sess key
            sessKeyResponse.status = jsonOb[Constants.STATUS.constanVal]?.toString()
                ?.replace("\"","") ?: Constants.STATUS_NOT_OK.constanVal

            if (sessKeyResponse.status == Constants.STATUS_OK.constanVal)
            {
                sessKeyResponse.sesskey = jsonOb[Constants.REF_JSON_OBJ_SESSKEY.constanVal]?.toString()
                    ?.replace("\"","") ?: Constants.RETURN_EMPTY.constanVal
            }
            else {
                sessKeyResponse.sesskey = Constants.RETURN_NULL.constanVal
                return 1// Indicate failure
            }

        }catch (e : Exception){
            Log.e("BookStoreDebug", "An error occur along third auth step:\n$e")
            return 1 // Indicate failure
        }

        return 0 // Indicate success
    }

    /**
     * Retrieves the decrypted app key from secure storage.
     *
     * @return The decrypted app key as a string.
     */
    private fun getAppKey(): String{
        val file = getSecretDocument(KeyName.SECRET_KEY_APPKEY,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_APPKEY
        )?.decodeToString().toString()
    }

    /**
     * Retrieves the decrypted OAuth key from secure storage.
     *
     * @return The decrypted OAuth key as a string.
     */
    private fun getOAuthKey(): String{
        val file = getSecretDocument(KeyName.SECRET_KEY_OAUTHKEY,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_OAUTHKEY
        )?.decodeToString().toString()
    }

    /**
     * Retrieves the decrypted password entered by user from secure storage.
     *
     * @return The decrypted password as a string.
     */
    private fun getPassword(): String{
        val file = getSecretDocument(KeyName.SECRET_KEY_PASSWORD,context)
        return CryptographerManager().decrypt(
            inputStream = FileInputStream(file),
            KeyName.SECRET_KEY_PASSWORD
        )?.decodeToString().toString()
    }

    /**
     * Encrypts the provided password and stores it in secure storage.
     *
     * @param password The plaintext password to encrypt.
     */
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

