package com.mevalera.feelslike.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.model.Condition
import com.mevalera.feelslike.domain.model.Current
import com.mevalera.feelslike.domain.model.Location
import com.mevalera.feelslike.ui.screens.HomeScreen
import com.mevalera.feelslike.ui.state.WeatherScreenUiState
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var sampleCityWeather: CityWeather

    @Before
    fun setup() {
        sampleCityWeather =
            CityWeather(
                location = Location(name = "London", country = "UK"),
                current =
                    Current(
                        tempC = 20.0,
                        humidity = 65,
                        uv = 3.0,
                        feelslikeC = 22.0,
                        condition =
                            Condition(
                                text = "Partly cloudy",
                                icon = "//cdn.weatherapi.com/weather/64x64/day/116.png",
                                code = 1003,
                            ),
                    ),
            )
    }

    @Composable
    private fun TestContent(
        state: WeatherScreenUiState,
        onSearchCity: (String) -> Unit = {},
        onSelectCityFromResults: (CityWeather) -> Unit = {},
    ) {
        MaterialTheme {
            HomeScreen(
                state = state,
                innerPadding = PaddingValues(),
                onSearchCity = onSearchCity,
                onSelectCityFromResults = onSelectCityFromResults,
            )
        }
    }

    @Test
    fun whenScreenLoads_searchBarIsDisplayed() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState = WeatherScreenUiState.CityWeatherState(),
                    ),
            )
        }

        composeTestRule.onNode(hasTestTag("searchBar")).assertIsDisplayed()
    }

    @Test
    fun whenNoCitySelected_showsNoCitySelectedContent() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                noCitySelected = true,
                                searchError = false,
                                otherError = false,
                            ),
                    ),
            )
        }

        composeTestRule.onNode(hasText("No City Selected")).assertExists()
        composeTestRule.onNode(hasText("Please Search For A City")).assertExists()
    }

    @Test
    fun whenLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                isLoading = true,
                            ),
                    ),
            )
        }

        composeTestRule.onNode(hasTestTag("loadingIndicator")).assertExists()
    }

    @Test
    fun whenCitySelected_showsCityWeather() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                selectedCity = sampleCityWeather,
                                isLoading = false,
                            ),
                    ),
            )
        }

        composeTestRule.onNode(hasText("London")).assertExists()
        composeTestRule.onNode(hasText("20°")).assertExists()
    }

    @Test
    fun whenSearchPerformed_callsOnSearchCity() {
        val onSearchCity = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState = WeatherScreenUiState.CityWeatherState(),
                    ),
                onSearchCity = onSearchCity,
            )
        }

        composeTestRule.onNode(hasTestTag("searchBar")).performTextInput("London")
        verify { onSearchCity("London") }
    }

    @Test
    fun whenCitySelectedFromResults_callsOnSelectCityFromResults() {
        val onSelectCityFromResults = mockk<(CityWeather) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                cities = listOf(sampleCityWeather),
                                isLoading = false,
                            ),
                    ),
                onSelectCityFromResults = onSelectCityFromResults,
            )
        }

        composeTestRule.onNode(hasText("London, UK")).performClick()
        verify { onSelectCityFromResults(sampleCityWeather) }
    }

    @Test
    fun whenSearchError_showsErrorMessage() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                searchError = true,
                                otherError = false,
                                isLoading = false,
                                noCitySelected = false,
                                cities = emptyList(),
                                selectedCity = null,
                            ),
                    ),
            )
        }

        composeTestRule.onNode(hasText("Network Error")).assertExists()
        composeTestRule
            .onNode(hasText("Failed to retrieve search results, please check your internet connection."))
            .assertExists()
    }

    @Test
    fun whenOtherError_showsGeneralErrorMessage() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                otherError = true,
                                searchError = false,
                                isLoading = false,
                                noCitySelected = false,
                                cities = emptyList(),
                                selectedCity = null,
                                selectedCityPersistedName = null,
                            ),
                    ),
            )
        }

        composeTestRule.onNode(hasText("Network Error")).assertExists()
        composeTestRule.onNode(hasText("Failed to search cities.")).assertExists()
    }

    @Test
    fun whenOtherErrorWithPersistedCity_showsSpecificErrorMessage() {
        composeTestRule.setContent {
            TestContent(
                state =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                otherError = true,
                                searchError = false,
                                isLoading = false,
                                noCitySelected = false,
                                cities = emptyList(),
                                selectedCity = null,
                                selectedCityPersistedName = "London",
                            ),
                    ),
            )
        }

        composeTestRule.onNode(hasText("Network Error")).assertExists()
        composeTestRule.onNode(hasText("Failed to get weather in London.")).assertExists()
    }
}
