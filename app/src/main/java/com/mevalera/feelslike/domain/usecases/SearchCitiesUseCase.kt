package com.mevalera.feelslike.domain.usecases

import com.mevalera.feelslike.domain.model.CityWeather

interface SearchCitiesUseCase {
    suspend operator fun invoke(query: String): Result<List<CityWeather>>
}
