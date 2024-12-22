package com.mevalera.feelslike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mevalera.feelslike.ui.screens.HomeScreen
import com.mevalera.feelslike.ui.theme.FeelsLikeTheme
import com.mevalera.feelslike.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeelsLikeTheme {
                val weatherViewModel: WeatherViewModel = hiltViewModel()
                val weatherState by weatherViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    weatherViewModel.onStart()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White,
                ) { innerPadding ->
                    HomeScreen(
                        state = weatherState,
                        innerPadding = innerPadding,
                        onSearchCity = { city ->
                            weatherViewModel.onSearchQueryChanged(city)
                        },
                        onSelectCityFromResults = {
                            weatherViewModel.selectCity(it)
                        },
                    )
                }
            }
        }
    }
}
