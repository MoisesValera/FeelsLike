package com.mevalera.feelslike.usecases

import com.mevalera.feelslike.data.repository.CityPreferencesRepository
import com.mevalera.feelslike.domain.usecases.SaveSelectedCityUseCaseImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SaveSelectedCityUseCaseImplTest {
    private lateinit var cityPreferencesRepository: CityPreferencesRepository
    private lateinit var useCase: SaveSelectedCityUseCaseImpl

    @Before
    fun setup() {
        cityPreferencesRepository = mockk()
        useCase = SaveSelectedCityUseCaseImpl(cityPreferencesRepository)
    }

    @Test
    fun `invoke should call repository`() =
        runBlocking {
            // Given
            val cityName = "London"
            coEvery { cityPreferencesRepository.saveSelectedCity(any()) } just Runs

            // When
            useCase.invoke(cityName)

            // Then
            coVerify { cityPreferencesRepository.saveSelectedCity(cityName) }
        }
}
