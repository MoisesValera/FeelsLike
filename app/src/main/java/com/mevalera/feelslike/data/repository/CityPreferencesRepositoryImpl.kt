package com.mevalera.feelslike.data.repository

import com.mevalera.feelslike.data.source.local.DataStoreService
import javax.inject.Inject

class CityPreferencesRepositoryImpl
    @Inject
    constructor(
        private val dataStoreService: DataStoreService,
    ) : CityPreferencesRepository {
        override suspend fun saveSelectedCity(cityName: String) {
            dataStoreService.saveSelectedCity(cityName)
        }

        override suspend fun getSelectedCity(): String? = dataStoreService.getSelectedCity()
    }
