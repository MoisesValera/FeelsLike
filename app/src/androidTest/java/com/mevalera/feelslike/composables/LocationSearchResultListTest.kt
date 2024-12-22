package com.mevalera.feelslike.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.model.Condition
import com.mevalera.feelslike.domain.model.Current
import com.mevalera.feelslike.domain.model.Location
import com.mevalera.feelslike.ui.composables.searchlocation.LocationSearchResultListContent
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocationSearchResultListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val onSelectCity = mockk<(CityWeather) -> Unit>(relaxed = true)
    private val citiesState = mutableStateOf<List<CityWeather>>(emptyList())

    private val londonWeather =
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

    private val parisWeather =
        CityWeather(
            location = Location(name = "Paris", country = "France"),
            current =
                Current(
                    tempC = 25.0,
                    humidity = 60,
                    uv = 4.0,
                    feelslikeC = 26.0,
                    condition =
                        Condition(
                            text = "Sunny",
                            icon = "//cdn.weatherapi.com/weather/64x64/day/113.png",
                            code = 1000,
                        ),
                ),
        )

    private val sampleCities = listOf(londonWeather, parisWeather)

    @Before
    fun setup() {
        citiesState.value = sampleCities

        composeTestRule.setContent {
            MaterialTheme {
                LocationSearchResultListContent(
                    cities = citiesState.value,
                    onSelectCity = onSelectCity,
                )
            }
        }
    }

    @Test
    fun whenCitiesProvided_showsCityList() {
        composeTestRule.onNodeWithTag("cityList").assertIsDisplayed()
        composeTestRule.onNodeWithText("London, UK").assertIsDisplayed()
        composeTestRule.onNodeWithText("Paris, France").assertIsDisplayed()
    }

    @Test
    fun whenCityItemClicked_callsOnSelectCity() {
        citiesState.value = listOf(londonWeather)

        composeTestRule.onNodeWithText("London, UK").performClick()
        verify { onSelectCity(londonWeather) }
    }

    @Test
    fun cityListItems_displayCorrectTemperature() {
        composeTestRule.onNodeWithText("20°").assertIsDisplayed()
        composeTestRule.onNodeWithText("25°").assertIsDisplayed()
    }

    @Test
    fun cityListItems_areClickable() {
        composeTestRule.onAllNodesWithTag("cityListItem").fetchSemanticsNodes().size.let { count ->
            repeat(count) {
                composeTestRule.onAllNodesWithTag("cityListItem")[it].assertHasClickAction()
            }
        }
    }

    @Test
    fun whenEmptyList_showsNothing() {
        citiesState.value = emptyList()

        composeTestRule.onNodeWithTag("cityList").assertExists()
        assert(composeTestRule.onAllNodesWithTag("cityListItem").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun listItem_showsCountryName() {
        composeTestRule.onNodeWithText("London, UK").assertIsDisplayed()
        composeTestRule.onNodeWithText("Paris, France").assertIsDisplayed()
    }

    @Test
    fun listItem_showsWeatherCondition() {
        composeTestRule.onNodeWithContentDescription("Partly cloudy").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Sunny").assertIsDisplayed()
    }
}
