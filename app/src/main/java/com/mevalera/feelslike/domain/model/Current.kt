package com.mevalera.feelslike.domain.model

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("temp_c")
    val tempC: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("uv")
    val uv: Double,
    @SerializedName("feelslike_c")
    val feelslikeC: Double,
    @SerializedName("condition")
    val condition: Condition,
)
