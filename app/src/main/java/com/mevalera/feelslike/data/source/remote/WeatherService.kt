package com.mevalera.feelslike.data.source.remote

import com.mevalera.feelslike.BuildConfig
import com.mevalera.feelslike.domain.model.CitySearchResult
import com.mevalera.feelslike.domain.model.CityWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("current.json")
    suspend fun getCityWeather(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") cityName: String,
    ): Response<CityWeather>

    @GET("search.json")
    suspend fun searchCitiesWeather(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") query: String,
    ): Response<List<CitySearchResult>>
}
