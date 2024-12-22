package com.mevalera.feelslike.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import com.mevalera.feelslike.R
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.model.Condition
import com.mevalera.feelslike.domain.model.Current
import com.mevalera.feelslike.domain.model.Location
import com.mevalera.feelslike.ui.composables.ErrorContent
import com.mevalera.feelslike.ui.composables.LoadingContent
import com.mevalera.feelslike.ui.composables.NoCitySelectedContent
import com.mevalera.feelslike.ui.composables.SelectedCityContent
import com.mevalera.feelslike.ui.composables.searchlocation.LocationSearchResultListContent
import com.mevalera.feelslike.ui.composables.searchlocation.SearchLocationBar
import com.mevalera.feelslike.ui.state.WeatherScreenUiState

@Composable
fun HomeScreen(
    state: WeatherScreenUiState,
    innerPadding: PaddingValues,
    onSearchCity: (String) -> Unit = {},
    onSelectCityFromResults: (CityWeather) -> Unit = {},
) {
    val cityWasSelected = remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier.padding(
                top = dimensionResource(R.dimen.home_screen_top_padding),
                start = dimensionResource(R.dimen.home_screen_horizontal_padding),
                end = dimensionResource(R.dimen.home_screen_horizontal_padding),
            ),
    ) {
        SearchLocationBar(
            onSearchCity = { city ->
                onSearchCity(city)
                cityWasSelected.value = false
            },
            cityWasSelected = cityWasSelected.value,
        )
        HomeScreenContent(
            state = state,
            modifier = Modifier.padding(innerPadding),
            onSelectCity = {
                onSelectCityFromResults(it)
                cityWasSelected.value = true
            },
        )
    }
}

@Composable
fun HomeScreenContent(
    state: WeatherScreenUiState,
    modifier: Modifier = Modifier,
    onSelectCity: (CityWeather) -> Unit = {},
) {
    val selectedCityState = state.cityWeatherState
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        with(selectedCityState) {
            when {
                isLoading -> LoadingContent()
                selectedCity != null -> SelectedCityContent(selectedCity)
                noCitySelected == true && searchError == false && otherError == false -> NoCitySelectedContent()
                !cities.isEmpty() ->
                    LocationSearchResultListContent(
                        cities = cities,
                        onSelectCity = {
                            onSelectCity(it)
                        },
                    )
                otherError == true -> {
                    if (selectedCityPersistedName == null) {
                        ErrorContent(
                            AnnotatedString(stringResource(R.string.error_failed_search_cities)),
                        )
                    } else {
                        ErrorContent(
                            AnnotatedString.fromHtml(
                                stringResource(R.string.error_failed_get_weather, selectedCityPersistedName),
                            ),
                        )
                    }
                }
                searchError == true -> {
                    ErrorContent(
                        AnnotatedString(stringResource(R.string.error_failed_search_results)),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            state =
                WeatherScreenUiState(
                    cityWeatherState =
                        WeatherScreenUiState.CityWeatherState(
                            selectedCity =
                                CityWeather(
                                    location = Location(name = "Hyderabad", country = "India"),
                                    current =
                                        Current(
                                            tempC = 31.0,
                                            humidity = 20,
                                            uv = 4.0,
                                            feelslikeC = 38.0,
                                            condition =
                                                Condition(
                                                    text = "Patchy rain nearby",
                                                    icon = "https://cdn.weatherapi.com/weather/64x64/day/176.png",
                                                    code = 1063,
                                                ),
                                        ),
                                ),
                        ),
                ),
            innerPadding = PaddingValues(),
        )
    }
}
