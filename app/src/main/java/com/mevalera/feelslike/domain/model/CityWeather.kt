package com.mevalera.feelslike.domain.model

import com.google.gson.annotations.SerializedName

data class CityWeather(
    @SerializedName("location")
    val location: Location,
    @SerializedName("current")
    val current: Current,
)
