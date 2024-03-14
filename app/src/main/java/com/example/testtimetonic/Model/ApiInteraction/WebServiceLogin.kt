package com.example.testtimetonic.Model.ApiInteraction

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface WebServiceLogin {

    @POST(".")
    suspend fun createAppKey(
        @Query("req") req: String,
        @Query("appname") appname: String
    ): Response<JsonElement>

    @POST(".")
    suspend fun createOauthkey(
        @Query("req") req: String,
        @Query("login") login: String,
        @Query("pwd") pwd: String,
        @Query("appkey") appkey: String
    ): Response<JsonElement>

    @POST(".")
    suspend fun createSessKey(
        @Query("req") req: String,
        @Query("o_u") oAuthUserid: String,
        @Query("oauthkey") oAuthKey: String
    ): Response<JsonElement>

}