package com.mevalera.feelslike.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreService
    @Inject
    constructor(
        private val context: Context,
    ) {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_preferences")

        private object PreferencesKeys {
            val SELECTED_CITY_NAME = stringPreferencesKey("selected_city_name")
        }

        suspend fun saveSelectedCity(cityName: String) {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.SELECTED_CITY_NAME] = cityName
            }
        }

        suspend fun getSelectedCity(): String? = context.dataStore.data.first()[PreferencesKeys.SELECTED_CITY_NAME]
    }
