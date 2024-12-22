package com.mevalera.feelslike.domain.usecases

import com.mevalera.feelslike.data.repository.CityPreferencesRepository
import javax.inject.Inject

class SaveSelectedCityUseCaseImpl
    @Inject
    constructor(
        private val cityPreferencesRepository: CityPreferencesRepository,
    ) : SaveSelectedCityUseCase {
        override suspend fun invoke(cityName: String) {
            cityPreferencesRepository.saveSelectedCity(cityName = cityName)
        }
    }
