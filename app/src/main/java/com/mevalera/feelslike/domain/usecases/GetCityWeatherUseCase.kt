package com.mevalera.feelslike.domain.usecases

import com.mevalera.feelslike.domain.model.CityWeather

interface GetCityWeatherUseCase {
    suspend operator fun invoke(cityName: String): Result<CityWeather>
}
