package com.mevalera.feelslike.viewmodel

import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.usecases.GetCityWeatherUseCase
import com.mevalera.feelslike.domain.usecases.GetSelectedCityUseCase
import com.mevalera.feelslike.domain.usecases.SaveSelectedCityUseCase
import com.mevalera.feelslike.domain.usecases.SearchCitiesUseCase
import com.mevalera.feelslike.ui.state.WeatherScreenUiState
import com.mevalera.feelslike.ui.viewmodel.WeatherViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private lateinit var viewModel: WeatherViewModel
    private val getCityWeatherUseCase: GetCityWeatherUseCase = mockk()
    private val searchCitiesUseCase: SearchCitiesUseCase = mockk()
    private val getSelectedCityUseCase: GetSelectedCityUseCase = mockk()
    private val saveSelectedCityUseCase: SaveSelectedCityUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel =
            WeatherViewModel(
                getCityWeatherUseCase,
                searchCitiesUseCase,
                getSelectedCityUseCase,
                saveSelectedCityUseCase,
                testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onStartShouldUpdateUiStateWhenCityIsSelected() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.onEach { states.add(it) }.launchIn(this)
                }

            val cityName = "New York"
            val cityWeather = mockk<CityWeather>()
            coEvery { getSelectedCityUseCase() } returns cityName
            coEvery { getCityWeatherUseCase(cityName) } returns Result.success(cityWeather)

            viewModel.onStart()
            advanceUntilIdle()
            job.cancel()

            assertTrue(states.isNotEmpty())
            val lastState = states.last()
            assertFalse(lastState.cityWeatherState.noCitySelected)
            assertEquals(cityName, lastState.cityWeatherState.selectedCityPersistedName)
        }

    @Test
    fun onStartShouldSetNoCitySelectedWhenNoCityIsSelected() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.onEach { states.add(it) }.launchIn(this)
                }

            coEvery { getSelectedCityUseCase() } returns null

            viewModel.onStart()
            advanceUntilIdle()
            job.cancel()

            assertTrue(states.isNotEmpty())
            val lastState = states.last()
            assertTrue(lastState.cityWeatherState.noCitySelected)
        }

    @Test
    fun onSearchQueryChangedUpdatesSearchQuery() =
        runTest {
            val query = "Lon"

            // Stub the searchCitiesUseCase to prevent the error
            coEvery { searchCitiesUseCase(query) } returns Result.success(emptyList())

            viewModel.onSearchQueryChanged(query)

            // Allow the debounce to process by advancing virtual time.
            advanceTimeBy(600)

            assertEquals(query, viewModel.searchQuery.value)
        }

    @Test
    fun performSearchUpdatesUiStateOnLoading() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.collect { state ->
                        states.add(state)
                    }
                }

            val query = "Lon"
            val cities = listOf(mockk<CityWeather>())

            coEvery { searchCitiesUseCase(query) } coAnswers {
                yield()
                Result.success(cities)
            }

            viewModel.performSearch(query)

            advanceUntilIdle()

            job.cancelAndJoin()

            assertTrue(states.any { it.cityWeatherState.isLoading }, "Should have at least one loading state")
        }

    @Test
    fun performSearchUpdatesUiStateOnSuccess() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.collect { state ->
                        states.add(state)
                    }
                }

            val query = "Lon"
            val cities = listOf(mockk<CityWeather>())

            coEvery { searchCitiesUseCase(query) } coAnswers {
                Result.success(cities)
            }

            viewModel.performSearch(query)

            advanceUntilIdle()

            job.cancelAndJoin()

            val lastState = states.last()
            assertFalse(lastState.cityWeatherState.isLoading)
            assertEquals(cities, lastState.cityWeatherState.cities)
        }

    @Test
    fun performSearchUpdatesUiStateOnFailure() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.collect { states.add(it) }
                }

            val query = "Lon"
            coEvery { searchCitiesUseCase(query) } coAnswers {
                Result.failure(Exception("Error"))
            }

            viewModel.performSearch(query)
            advanceUntilIdle()

            job.cancelAndJoin()

            val lastState = states.last()
            assertFalse(lastState.cityWeatherState.isLoading)
            assertTrue(lastState.cityWeatherState.searchError)
        }

    @Test
    fun getCityWeatherUpdatesUiStateOnSuccess() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.onEach { states.add(it) }.launchIn(this)
                }

            val cityName = "London"
            val cityWeather = mockk<CityWeather>()

            coEvery { getSelectedCityUseCase() } returns cityName
            coEvery { getCityWeatherUseCase(cityName) } returns Result.success(cityWeather)

            viewModel.getCityWeather(cityName)
            advanceUntilIdle()
            job.cancel()

            assertTrue(states.isNotEmpty())
            val lastState = states.last()
            assertFalse(lastState.cityWeatherState.isLoading)
            assertEquals(cityWeather, lastState.cityWeatherState.selectedCity)
        }

    @Test
    fun getCityWeatherUpdatesUiStateOnFailure() =
        runTest {
            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState.onEach { states.add(it) }.launchIn(this)
                }

            val cityName = "London"

            coEvery { getSelectedCityUseCase() } returns cityName
            coEvery { getCityWeatherUseCase(cityName) } returns Result.failure(Exception("Error"))

            viewModel.getCityWeather(cityName)
            advanceUntilIdle()
            job.cancel()

            assertTrue(states.isNotEmpty())
            val lastState = states.last()
            assertFalse(lastState.cityWeatherState.isLoading)
            assertTrue(lastState.cityWeatherState.otherError)
        }

    @Test
    fun selectCityUpdatesUiStateAndSavesCity() =
        runTest {
            val cityWeather =
                mockk<CityWeather> {
                    every { location.name } returns "Paris"
                }

            coEvery { getSelectedCityUseCase() } returns null
            coEvery { saveSelectedCityUseCase("Paris") } just Runs

            val states = mutableListOf<WeatherScreenUiState>()
            val job =
                launch {
                    viewModel.uiState
                        .onEach { states.add(it) }
                        .launchIn(this)
                }

            viewModel.selectCity(cityWeather)
            advanceUntilIdle()

            job.cancel()

            assertTrue(states.isNotEmpty())
            val lastState = states.last()
            assertEquals(cityWeather, lastState.cityWeatherState.selectedCity)

            coVerify { saveSelectedCityUseCase("Paris") }
        }

    @Test
    fun `onSearchQueryChanged should not trigger search if query is empty`() =
        runTest {
            // Given
            val query = ""

            // When
            viewModel.onSearchQueryChanged(query)
            advanceTimeBy(600)

            // Then
            coVerify(exactly = 0) { searchCitiesUseCase(any()) }
        }
}
