package com.mevalera.feelslike.data.repository

import com.mevalera.feelslike.domain.model.CityWeather

interface WeatherRepository {
    suspend fun getCityWeather(cityName: String): Result<CityWeather>

    suspend fun searchCities(query: String): Result<List<CityWeather>>
}
