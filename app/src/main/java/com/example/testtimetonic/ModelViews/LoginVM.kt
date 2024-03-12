package com.example.testtimetonic.ModelViews

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class LoginVM: ViewModel() {
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

    suspend fun onLoginSelected(navToLanding:() -> Unit) {
        _isLoading.value = true

        //TODO implement the logic for login with the API calls
        delay(4000)

        navToLanding()

        _isLoading.value = false
    }

}