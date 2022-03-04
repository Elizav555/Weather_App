package com.example.weatherApp.domain.entities

data class CityWeather(
    val id: Int,
    val name: String,
    val temp: Int,
    val feelsLikeTemp: Int,
    val weatherDesc: String,
    val weatherIcon: String,
    val windDir: String,
    val windSpeed: String,
    val humidity: Int,
    val pressure: Int,
)
