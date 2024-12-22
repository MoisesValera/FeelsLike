package com.mevalera.feelslike.domain.model

import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("text")
    val text: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("code")
    val code: Int,
) {
    val highResIcon: String
        get() = "https:" + icon.replace("64x64", "128x128")
}
