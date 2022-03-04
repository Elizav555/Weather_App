package com.example.weatherApp.domain

import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates

interface WeatherRepository {

    suspend fun getWeather(cityName: String): CityWeather

    suspend fun getWeatherNearLocation(coordinates: Coordinates): List<CityWeather>

    suspend fun getWeather(cityId: Int): CityWeather
}
