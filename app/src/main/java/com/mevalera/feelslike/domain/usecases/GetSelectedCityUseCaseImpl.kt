package com.mevalera.feelslike.domain.usecases

import com.mevalera.feelslike.data.repository.CityPreferencesRepository
import javax.inject.Inject

class GetSelectedCityUseCaseImpl
    @Inject
    constructor(
        private val cityPreferencesRepository: CityPreferencesRepository,
    ) : GetSelectedCityUseCase {
        override suspend fun invoke(): String? = cityPreferencesRepository.getSelectedCity()
    }
