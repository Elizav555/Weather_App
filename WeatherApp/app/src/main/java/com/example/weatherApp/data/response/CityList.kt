package com.example.weatherApp.data.response

data class CityList(
    val cod: String,
    val count: Int,
    val list: List<City>,
    val message: String
)
