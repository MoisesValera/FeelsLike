package com.mevalera.feelslike.usecases

import com.mevalera.feelslike.data.repository.CityPreferencesRepository
import com.mevalera.feelslike.domain.usecases.GetSelectedCityUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetSelectedCityUseCaseImplTest {
    private lateinit var cityPreferencesRepository: CityPreferencesRepository
    private lateinit var useCase: GetSelectedCityUseCaseImpl

    @Before
    fun setup() {
        cityPreferencesRepository = mockk()
        useCase = GetSelectedCityUseCaseImpl(cityPreferencesRepository)
    }

    @Test
    fun `invoke should return repository result`() =
        runBlocking {
            // Given
            val expectedCity = "Paris"
            coEvery { cityPreferencesRepository.getSelectedCity() } returns expectedCity

            // When
            val result = useCase.invoke()

            // Then
            assertEquals(expectedCity, result)
            coVerify { cityPreferencesRepository.getSelectedCity() }
        }
}
