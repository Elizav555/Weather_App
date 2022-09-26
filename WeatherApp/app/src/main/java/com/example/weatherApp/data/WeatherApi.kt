package com.example.weatherApp.data

import com.example.weatherApp.data.response.City
import com.example.weatherApp.data.response.CityList
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather?")
    suspend fun getWeather(@Query("q") city: String): City

    @GET("weather?")
    suspend fun getWeather(@Query("id") cityId: Int): City

    @GET("find?cnt=10")
    suspend fun getWeatherNearLocation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
    ): CityList
}
