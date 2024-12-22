package composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mevalera.feelslike.domain.model.CityWeather
import com.mevalera.feelslike.domain.model.Condition
import com.mevalera.feelslike.domain.model.Current
import com.mevalera.feelslike.domain.model.Location
import com.mevalera.feelslike.ui.composables.searchlocation.LocationSearchResultListContent

@Preview(showBackground = true)
@Composable
fun LocationSearchResultListContentPreview() {
    val sampleCities =
        listOf(
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
            CityWeather(
                location = Location(name = "Lima", country = "Peru"),
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
