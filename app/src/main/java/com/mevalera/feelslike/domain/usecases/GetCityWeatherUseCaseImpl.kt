package com.mevalera.feelslike.domain.usecases

import com.mevalera.feelslike.data.repository.WeatherRepository
import com.mevalera.feelslike.domain.model.CityWeather
import javax.inject.Inject

class GetCityWeatherUseCaseImpl
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
    ) : GetCityWeatherUseCase {
        override suspend fun invoke(cityName: String): Result<CityWeather> = weatherRepository.getCityWeather(cityName)
    }
