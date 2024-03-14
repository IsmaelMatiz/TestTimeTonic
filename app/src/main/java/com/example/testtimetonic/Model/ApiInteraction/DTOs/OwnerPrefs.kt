package com.example.testtimetonic.Model.ApiInteraction.DTOs

import com.google.gson.annotations.SerializedName

class OwnerPrefs {
    @SerializedName("oCoverImg") var oCoverImg: String?  = null
    @SerializedName("title"    ) var title    : String?  = null
    override fun toString(): String {
        return "OwnerPrefs(oCoverImg=$oCoverImg, title=$title)"
    }


}