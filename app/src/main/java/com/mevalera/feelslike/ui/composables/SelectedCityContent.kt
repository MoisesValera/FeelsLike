package com.mevalera.feelslike.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mevalera.feelslike.R
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.model.Condition
import com.mevalera.feelslike.domain.model.Current
import com.mevalera.feelslike.domain.model.Location
import com.mevalera.feelslike.ui.theme.poppinsFamily

@Composable
fun SelectedCityContent(selectedCity: CityWeather) {
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        keyboardManager?.hide()
        focusManager.clearFocus()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.error_spacer_height)),
        ) {
            AsyncImage(
                model = selectedCity.current.condition.highResIcon,
                contentDescription = selectedCity.current.condition.text,
                modifier = Modifier.size(dimensionResource(R.dimen.weather_icon_size)),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.city_name_right_icon_padding)),
                modifier = Modifier.padding(top = dimensionResource(R.dimen.city_name_top_padding)),
            ) {
                Text(
                    text = selectedCity.location.name,
                    fontSize = dimensionResource(R.dimen.city_name_text_size).value.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                )
                Image(
                    painter = painterResource(R.drawable.plane_icon),
                    contentDescription = "",
                )
            }
        }

        Text(
            text = stringResource(R.string.temperature_format, selectedCity.current.tempC.toInt()),
            fontSize = dimensionResource(R.dimen.temperature_text_size).value.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.city_name_top_padding)),
        )

        Card(
            modifier =
                Modifier
                    .padding(
                        start = dimensionResource(R.dimen.weather_card_horizontal_padding),
                        end = dimensionResource(R.dimen.weather_card_horizontal_padding),
                        top = dimensionResource(R.dimen.weather_card_top_padding),
                    ).wrapContentWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = Color(0xFFF2F2F2),
                ),
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(dimensionResource(R.dimen.weather_card_content_padding))
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                WeatherInfo(
                    label = stringResource(R.string.weather_info_humidity),
                    value =
                        stringResource(
                            R.string.weather_info_humidity_format,
                            selectedCity.current.humidity,
                        ),
                )
                WeatherInfo(
                    label = stringResource(R.string.weather_info_uv),
                    value = selectedCity.current.uv.toString(),
                )
                WeatherInfo(
                    label = stringResource(R.string.weather_info_feels_like),
                    value =
                        stringResource(
                            R.string.weather_info_feels_like_format,
                            selectedCity.current.feelslikeC.toInt(),
                        ),
                )
            }
        }
    }
}

@Composable
fun WeatherInfo(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            fontSize = dimensionResource(R.dimen.weather_info_label_text_size).value.sp,
            color = Color(0xFFC4C4C4),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            fontSize = dimensionResource(R.dimen.weather_info_value_text_size).value.sp,
            color = Color(0xFF9A9A9A),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedCityPreview() {
    MaterialTheme {
        SelectedCityContent(
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
        )
    }
}
