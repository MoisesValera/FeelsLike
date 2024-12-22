package com.mevalera.feelslike.ui.composables.searchlocation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mevalera.feelslike.R
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.model.Condition
import com.mevalera.feelslike.domain.model.Current
import com.mevalera.feelslike.domain.model.Location

@Composable
fun LocationSearchResultListContent(
    cities: List<CityWeather>,
    onSelectCity: (CityWeather) -> Unit = {},
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .testTag("cityList"),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.search_results_vertical_spacing)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.search_results_content_padding)),
    ) {
        items(cities) { cityWeather ->
            LocationSearchResultItem(
                cityWeather = cityWeather,
                onSelectCity = { selectedCity ->
                    onSelectCity(selectedCity)
                },
            )
        }
    }
}

@Composable
fun LocationSearchResultItem(
    cityWeather: CityWeather,
    onSelectCity: (CityWeather) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onSelectCity(cityWeather) },
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("cityListItem"),
            color = Color(0xFFF2F2F2),
            shape = RoundedCornerShape(dimensionResource(R.dimen.search_result_corner_radius)),
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(dimensionResource(R.dimen.search_result_content_padding))
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(0.7f)
                            .padding(end = dimensionResource(R.dimen.search_result_content_padding)),
                ) {
                    Text(
                        text =
                            stringResource(
                                R.string.city_location_format,
                                cityWeather.location.name,
                                cityWeather.location.country ?: ""
                            ),
                        fontSize = dimensionResource(R.dimen.search_result_city_text_size).value.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text =
                            stringResource(
                                R.string.temperature_format,
                                cityWeather.current.tempC.toInt(),
                            ),
                        fontSize = dimensionResource(R.dimen.search_result_temperature_text_size).value.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                AsyncImage(
                    model = cityWeather.current.condition.highResIcon,
                    contentDescription = cityWeather.current.condition.text,
                    modifier =
                        Modifier
                            .weight(0.3f)
                            .size(dimensionResource(R.dimen.search_result_weather_icon_size)),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSearchResultListContentPreview() {
    val sampleCities =
        listOf(
            CityWeather(
                location = Location(name = "London", country = "United Kingdom"),
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
            CityWeather(
                location = Location(name = "Valle del Cauca", country = "Colombia"),
                current =
                    Current(
                        tempC = 22.0,
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

    LocationSearchResultListContent(
        cities = sampleCities,
    )
}
