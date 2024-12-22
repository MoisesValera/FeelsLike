package com.mevalera.feelslike.ui.state

import com.mevalera.feelslike.domain.model.CityWeather

data class WeatherScreenUiState(
    val cityWeatherState: CityWeatherState = CityWeatherState(),
) {
    data class CityWeatherState(
        val isLoading: Boolean = false,
        val selectedCity: CityWeather? = null,
        val selectedCityPersistedName: String? = null,
        val noCitySelected: Boolean = false,
        val cities: List<CityWeather> = emptyList(),
        val otherError: Boolean = false,
        val searchError: Boolean = false,
    )
}
