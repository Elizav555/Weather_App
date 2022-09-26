package com.example.weatherApp.data.mapper

import com.example.weatherApp.data.response.City
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.DirectionManager
import kotlin.math.roundToInt

class CityMapper {
    fun map(response: City): CityWeather = CityWeather(
        id = response.id,
        name = response.name,
        temp = response.main.temp.roundToInt(),
        feelsLikeTemp = response.main.feels_like.roundToInt(),
        weatherDesc = response.weather[0].description.replaceFirstChar { it.uppercase() },
        weatherIcon = response.weather[0].icon,
        windDir = DirectionManager().degreesToDirection(response.wind.deg),
        windSpeed = "%.1f".format(response.wind.speed),
        humidity = response.main.humidity,
        pressure = response.main.pressure,
    )
}
