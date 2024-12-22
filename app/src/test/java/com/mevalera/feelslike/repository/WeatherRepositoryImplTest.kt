package com.mevalera.feelslike.repository

import com.mevalera.feelslike.data.repository.WeatherRepositoryImpl
import com.mevalera.feelslike.data.source.remote.WeatherService
import com.mevalera.feelslike.domain.model.CitySearchResult
import com.mevalera.feelslike.domain.model.CityWeather
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WeatherRepositoryImplTest {
    private lateinit var weatherService: WeatherService
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setup() {
        weatherService = mockk()
        repository = WeatherRepositoryImpl(weatherService)
    }

    @Test
    fun `getCityWeather should return success when service call is successful`() =
        runBlocking {
            // Given
            val cityName = "London"
            val mockWeather = mockk<CityWeather>()
            val response = Response.success(mockWeather)
            coEvery { weatherService.getCityWeather(cityName = cityName) } returns response

            // When
            val result = repository.getCityWeather(cityName)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(mockWeather, result.getOrNull())
            coVerify { weatherService.getCityWeather(cityName = cityName) }
        }

    @Test
    fun `getCityWeather should return failure when service call fails`() =
        runBlocking {
            // Given
            val cityName = "London"
            val response = Response.error<CityWeather>(404, mockk(relaxed = true))
            coEvery { weatherService.getCityWeather(cityName = cityName) } returns response

            // When
            val result = repository.getCityWeather(cityName)

            // Then
            assertTrue(result.isFailure)
            assertNotNull(result.exceptionOrNull())
            coVerify { weatherService.getCityWeather(cityName = cityName) }
        }

    @Test
    fun `searchCities should return success with populated weather data`() =
        runBlocking {
            // Given
            val query = "Lon"
            val citySearchResult = CitySearchResult(id = 123, name = "London")
            val cityWeather = mockk<CityWeather>()

            coEvery { weatherService.searchCitiesWeather(query = query) } returns Response.success(listOf(citySearchResult))
            coEvery { weatherService.getCityWeather(cityName = "id:123") } returns Response.success(cityWeather)

            // When
            val result = repository.searchCities(query)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(listOf(cityWeather), result.getOrNull())
            coVerify {
                weatherService.searchCitiesWeather(query = query)
                weatherService.getCityWeather(cityName = "id:123")
            }
        }

    @Test
    fun `getCityWeather should return failure when service throws exception`() =
        runBlocking {
            // Given
            val cityName = "London"
            coEvery { weatherService.getCityWeather(cityName = cityName) } throws Exception("Network error")

            // When
            val result = repository.getCityWeather(cityName)

            // Then
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is Exception)
        }

    @Test
    fun `searchCities should return failure when search service call fails`() =
        runBlocking {
            // Given
            val query = "Lon"
            coEvery { weatherService.searchCitiesWeather(query = query) } returns Response.error(500, mockk(relaxed = true))

            // When
            val result = repository.searchCities(query)

            // Then
            assertTrue(result.isFailure)
        }

    @Test
    fun `searchCities should return failure when weather service call fails`() =
        runBlocking {
            // Given
            val query = "Lon"
            val citySearchResult = CitySearchResult(id = 123, name = "London")

            coEvery { weatherService.searchCitiesWeather(query = query) } returns Response.success(listOf(citySearchResult))
            coEvery { weatherService.getCityWeather(cityName = "id:123") } returns Response.error(404, mockk(relaxed = true))

            // When
            val result = repository.searchCities(query)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(emptyList(), result.getOrNull())
        }
}
