package com.mevalera.feelslike.repository

import com.mevalera.feelslike.data.repository.CityPreferencesRepositoryImpl
import com.mevalera.feelslike.data.source.local.DataStoreService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CityPreferencesRepositoryImplTest {
    private lateinit var dataStoreService: DataStoreService
    private lateinit var repository: CityPreferencesRepositoryImpl

    @Before
    fun setup() {
        dataStoreService = mockk()
        repository = CityPreferencesRepositoryImpl(dataStoreService)
    }

    @Test
    fun `saveSelectedCity should call dataStoreService`() =
        runBlocking {
            // Given
            val cityName = "London"
            coEvery { dataStoreService.saveSelectedCity(any()) } just Runs

            // When
            repository.saveSelectedCity(cityName)

            // Then
            coVerify { dataStoreService.saveSelectedCity(cityName) }
        }

    @Test
    fun `getSelectedCity should return value from dataStoreService`() =
        runBlocking {
            // Given
            val expectedCity = "Paris"
            coEvery { dataStoreService.getSelectedCity() } returns expectedCity

            // When
            val result = repository.getSelectedCity()

            // Then
            assertEquals(expectedCity, result)
            coVerify { dataStoreService.getSelectedCity() }
        }
}
