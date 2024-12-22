package com.mevalera.feelslike.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.usecases.GetCityWeatherUseCase
import com.mevalera.feelslike.domain.usecases.GetSelectedCityUseCase
import com.mevalera.feelslike.domain.usecases.SaveSelectedCityUseCase
import com.mevalera.feelslike.domain.usecases.SearchCitiesUseCase
import com.mevalera.feelslike.ui.state.WeatherScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val getCityWeatherUseCase: GetCityWeatherUseCase,
        private val searchCitiesUseCase: SearchCitiesUseCase,
        private val getSelectedCityUseCase: GetSelectedCityUseCase,
        private val saveSelectedCityUseCase: SaveSelectedCityUseCase,
        private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                WeatherScreenUiState(
                    cityWeatherState =
                        WeatherScreenUiState.CityWeatherState(),
                ),
            )
        val uiState: StateFlow<WeatherScreenUiState> = _uiState.asStateFlow()

        val searchQuery = MutableStateFlow("")

        init {
            viewModelScope.launch(dispatcher) {
                searchQuery
                    .debounce(600)
                    .distinctUntilChanged()
                    .filter { query ->
                        query.length >= 3
                    }.collect { query ->
                        performSearch(query)
                    }
            }
        }

        fun onSearchQueryChanged(query: String) {
            searchQuery.value = query
        }

        fun onStart() {
            viewModelScope.launch {
                getSelectedCityUseCase()?.let { cityName ->
                    getCityWeather(cityName)
                    _uiState.update {
                        it.copy(
                            cityWeatherState =
                                it.cityWeatherState.copy(
                                    noCitySelected = false,
                                    selectedCityPersistedName = cityName,
                                ),
                        )
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(
                            cityWeatherState =
                                it.cityWeatherState.copy(
                                    noCitySelected = true,
                                ),
                        )
                    }
                }
            }
        }

        fun performSearch(query: String) {
            viewModelScope.launch(dispatcher) {
                _uiState.update {
                    it.copy(
                        cityWeatherState =
                            it.cityWeatherState.copy(
                                isLoading = true,
                                selectedCity = null,
                                searchError = false,
                            ),
                    )
                }
                searchCitiesUseCase(query)
                    .onSuccess { cities ->
                        _uiState.update {
                            it.copy(
                                cityWeatherState =
                                    it.cityWeatherState.copy(
                                        isLoading = false,
                                        noCitySelected = false,
                                        cities = cities,
                                        searchError = false,
                                        otherError = false,
                                    ),
                            )
                        }
                    }.onFailure { _ ->
                        _uiState.update {
                            it.copy(
                                cityWeatherState =
                                    it.cityWeatherState.copy(
                                        searchError = true,
                                        otherError = false,
                                        isLoading = false,
                                    ),
                            )
                        }
                    }
            }
        }

        fun getCityWeather(cityName: String) {
            viewModelScope.launch(dispatcher) {
                _uiState.update {
                    it.copy(
                        cityWeatherState =
                            it.cityWeatherState.copy(
                                isLoading = true,
                            ),
                    )
                }

                getCityWeatherUseCase(cityName)
                    .onSuccess { cityWeather ->
                        _uiState.update {
                            it.copy(
                                cityWeatherState =
                                    it.cityWeatherState.copy(
                                        isLoading = false,
                                        selectedCity = cityWeather,
                                    ),
                            )
                        }
                    }.onFailure { _ ->
                        _uiState.update {
                            it.copy(
                                cityWeatherState =
                                    it.cityWeatherState.copy(
                                        isLoading = false,
                                        otherError = true,
                                    ),
                            )
                        }
                    }
            }
        }

        fun selectCity(cityWeather: CityWeather) {
            viewModelScope.launch(dispatcher) {
                saveSelectedCityUseCase(cityWeather.location.name)

                _uiState.value =
                    WeatherScreenUiState(
                        cityWeatherState =
                            WeatherScreenUiState.CityWeatherState(
                                isLoading = false,
                                cities = emptyList(),
                                selectedCity = cityWeather,
                            ),
                    )
            }
        }
    }
