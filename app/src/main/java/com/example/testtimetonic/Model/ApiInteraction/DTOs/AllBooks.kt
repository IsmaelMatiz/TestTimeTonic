package com.example.testtimetonic.Model.ApiInteraction.DTOs

import com.google.gson.annotations.SerializedName

class AllBooks {
    @SerializedName("contacts"   ) var contacts   : ArrayList<Contact> = arrayListOf()
    @SerializedName("books"      ) var books      : ArrayList<Book>  = arrayListOf()

}