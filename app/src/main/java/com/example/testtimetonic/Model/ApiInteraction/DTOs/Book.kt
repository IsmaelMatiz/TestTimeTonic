package com.example.testtimetonic.Model.ApiInteraction.DTOs

import com.google.gson.annotations.SerializedName

class Book {
    @SerializedName("contact_u_c"     ) var contactUC       : String?            = null
    @SerializedName("ownerPrefs"      ) var ownerPrefs      : OwnerPrefs?        = OwnerPrefs()

}