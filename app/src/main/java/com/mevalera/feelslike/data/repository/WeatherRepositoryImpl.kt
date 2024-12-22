package com.mevalera.feelslike.data.repository

import com.mevalera.feelslike.data.source.remote.WeatherService
import com.mevalera.feelslike.domain.model.CitySearchResult
import com.mevalera.feelslike.domain.model.CityWeather
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class WeatherRepositoryImpl
    @Inject
    constructor(
        private val weatherService: WeatherService,
    ) : WeatherRepository {
        override suspend fun getCityWeather(cityName: String): Result<CityWeather> =
            try {
                val response = weatherService.getCityWeather(cityName = cityName)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun searchCities(query: String): Result<List<CityWeather>> =
            try {
                val response = weatherService.searchCitiesWeather(query = query)
                if (response.isSuccessful && response.body() != null) {
                    val cities = response.body()!!
                    coroutineScope {
                        val weatherResults =
                            cities
                                .map { city: CitySearchResult ->
                                    async {
                                        getCityWeather("id:${city.id}").getOrNull()
                                    }
                                }.awaitAll()

                        val filledCities = weatherResults.filterNotNull()

                        Result.success(filledCities)
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
