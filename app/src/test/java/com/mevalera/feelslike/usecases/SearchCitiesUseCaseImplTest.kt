package com.mevalera.feelslike.usecases

import com.mevalera.feelslike.data.repository.WeatherRepository
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.usecases.SearchCitiesUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SearchCitiesUseCaseImplTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var useCase: SearchCitiesUseCaseImpl

    @Before
    fun setup() {
        weatherRepository = mockk()
        useCase = SearchCitiesUseCaseImpl(weatherRepository)
    }

    @Test
    fun `invoke should return empty list when query is less than 3 characters`() =
        runBlocking {
            // Given
            val query = "Lo"

            // When
            val result = useCase.invoke(query)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(emptyList<CityWeather>(), result.getOrNull())
            coVerify(exactly = 0) { weatherRepository.searchCities(any()) }
        }

    @Test
    fun `invoke should call repository when query is 3 or more characters`() =
        runBlocking {
            // Given
            val query = "London"
            val mockWeatherList = listOf(mockk<CityWeather>())
            val expectedResult = Result.success(mockWeatherList)
            coEvery { weatherRepository.searchCities(query) } returns expectedResult

            // When
            val result = useCase.invoke(query)

            // Then
            assertEquals(expectedResult, result)
            coVerify { weatherRepository.searchCities(query) }
        }

    @Test
    fun `invoke should return failure when repository fails`() =
        runBlocking {
            // Given
            val query = "London"
            coEvery { weatherRepository.searchCities(query) } returns Result.failure(Exception("Error"))

            // When
            val result = useCase.invoke(query)

            // Then
            assertTrue(result.isFailure)
        }

    @Test
    fun `invoke should handle empty query`() =
        runBlocking {
            // Given
            val query = ""

            // When
            val result = useCase.invoke(query)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(emptyList(), result.getOrNull())
        }
}
