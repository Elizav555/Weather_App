package com.example.weatherApp.data.response

import com.example.weatherApp.data.response.City

data class CityList(
    val cod: String,
    val count: Int,
    val list: List<City>,
    val message: String
)
