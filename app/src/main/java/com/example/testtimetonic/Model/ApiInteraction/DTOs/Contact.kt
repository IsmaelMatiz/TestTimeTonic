package com.example.testtimetonic.Model.ApiInteraction.DTOs

import com.google.gson.annotations.SerializedName

class Contact {
    @SerializedName("u_c") var userId : String?  = null
    @SerializedName("firstName") var firstName : String?  = null
    @SerializedName("lastName") var lastName : String?  = null

}