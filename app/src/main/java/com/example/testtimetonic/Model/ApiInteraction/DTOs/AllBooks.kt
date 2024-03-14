package com.example.testtimetonic.Model.ApiInteraction.DTOs

import com.google.gson.annotations.SerializedName

class AllBooks {
    @SerializedName("contacts"   ) var contacts   : ArrayList<String> = arrayListOf()
    @SerializedName("books"      ) var books      : ArrayList<Book>  = arrayListOf()
    override fun toString(): String {
        return "AllBooks(contacts=$contacts, books=$books)"
    }


}