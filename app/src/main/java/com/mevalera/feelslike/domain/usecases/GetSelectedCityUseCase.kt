package com.mevalera.feelslike.domain.usecases

interface GetSelectedCityUseCase {
    suspend operator fun invoke(): String?
}
