package com.example.testtimetonic.Model.ApiInteraction.Responses

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface WebServiceLanding {

    @POST(".")
    suspend fun getAllBooks(
        @Query("req") req: String,
        @Query("o_u") oAuthUserid: String,
        @Query("u_c") Userid: String,
        @Query("sesskey") sesskey: String,
    ): Response<JsonElement>
}