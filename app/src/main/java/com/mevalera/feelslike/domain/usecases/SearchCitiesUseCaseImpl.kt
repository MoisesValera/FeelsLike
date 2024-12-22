package com.mevalera.feelslike.domain.usecases

import com.mevalera.feelslike.data.repository.WeatherRepository
import com.mevalera.feelslike.domain.model.CityWeather
import javax.inject.Inject

class SearchCitiesUseCaseImpl
    @Inject
    constructor(
        private val weatherRepository: WeatherRepository,
    ) : SearchCitiesUseCase {
        override suspend fun invoke(query: String): Result<List<CityWeather>> {
            if (query.length < 3) {
                return Result.success(emptyList())
            }
            return weatherRepository.searchCities(query)
        }
    }
