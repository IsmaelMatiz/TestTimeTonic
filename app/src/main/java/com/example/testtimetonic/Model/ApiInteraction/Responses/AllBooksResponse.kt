package com.example.testtimetonic.Model.ApiInteraction.Responses

import com.example.testtimetonic.Model.ApiInteraction.DTOs.AllBooks
import com.google.gson.annotations.SerializedName

class AllBooksResponse {
    @SerializedName("status"     ) var status     : String?   = null
    @SerializedName("allBooks"   ) var allBooks   : AllBooks? = AllBooks()

    override fun toString(): String {
        return "AllBooksResponse(status=$status, allBooks=$allBooks)"
    }

}