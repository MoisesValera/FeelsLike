package com.mevalera.feelslike.data.repository

interface CityPreferencesRepository {
    suspend fun saveSelectedCity(cityName: String)

    suspend fun getSelectedCity(): String?
}
