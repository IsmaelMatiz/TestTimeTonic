package com.example.testtimetonic.Model.ApiInteraction

import android.content.Context
import com.example.testtimetonic.Model.Constants
import com.example.testtimetonic.R
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A Retrofit client responsible for creating a service instance for interaction with the login web service.
 */
class RetroFitClient() {
    /**
     * Lazily initializes a [WebServiceLogin] instance for making network calls to the login service.
     */
    val webServiceLogin: WebServiceLogin by lazy {
        // Create a Retrofit instance for the login service with specified configuration
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL.constanVal) // Set the base URL for API calls
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())) // Use Gson for JSON parsing
            .callFactory( // Configure network timeouts for resilience
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            // Create an instance of the WebServiceLogin API interface
            .create(WebServiceLogin::class.java)
    }
}
