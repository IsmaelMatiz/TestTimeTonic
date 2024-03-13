package com.example.testtimetonic.Model.ApiInteraction

import android.content.Context
import com.example.testtimetonic.R
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetroFitClient(val context: Context) {
    val webServiceLogin: WebServiceLogin by lazy {
        Retrofit.Builder()
            .baseUrl(context.getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .callFactory(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(WebServiceLogin::class.java)
    }
}
