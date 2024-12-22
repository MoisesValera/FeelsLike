package com.mevalera.feelslike.domain.usecases

interface SaveSelectedCityUseCase {
    suspend operator fun invoke(cityName: String)
}
