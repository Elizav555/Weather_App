package com.example.weatherApp.data.response

data class City(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val id: Int,
    val main: Main,
    val name: String,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
