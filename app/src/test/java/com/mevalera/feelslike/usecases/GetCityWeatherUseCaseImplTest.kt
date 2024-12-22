package com.mevalera.feelslike.usecases

import com.mevalera.feelslike.data.repository.WeatherRepository
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.usecases.GetCityWeatherUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetCityWeatherUseCaseImplTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var useCase: GetCityWeatherUseCaseImpl

    @Before
    fun setup() {
        weatherRepository = mockk()
        useCase = GetCityWeatherUseCaseImpl(weatherRepository)
    }

    @Test
    fun `invoke should return repository result`() =
        runBlocking {
            // Given
            val cityName = "London"
            val mockWeather = mockk<CityWeather>()
            val expectedResult = Result.success(mockWeather)
            coEvery { weatherRepository.getCityWeather(cityName) } returns expectedResult

            // When
            val result = useCase.invoke(cityName)

            // Then
            assertEquals(expectedResult, result)
            coVerify { weatherRepository.getCityWeather(cityName) }
        }

    @Test
    fun `invoke should return failure when repository fails`() =
        runBlocking {
            // Given
            val cityName = "London"
            coEvery { weatherRepository.getCityWeather(cityName) } returns Result.failure(Exception("Error"))

            // When
            val result = useCase.invoke(cityName)

            // Then
            assertTrue(result.isFailure)
        }

    @Test
    fun `invoke should handle empty city name`() =
        runBlocking {
            // Given
            val cityName = ""
            coEvery { weatherRepository.getCityWeather(cityName) } returns Result.failure(Exception("Invalid city name"))

            // When
            val result = useCase.invoke(cityName)

            // Then
            assertTrue(result.isFailure)
        }
}
