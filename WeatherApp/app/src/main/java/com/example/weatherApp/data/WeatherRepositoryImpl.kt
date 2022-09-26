package com.example.weatherApp.data

import com.example.weatherApp.data.mapper.CityMapper
import com.example.weatherApp.domain.WeatherRepository
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates

class WeatherRepositoryImpl(private val api: WeatherApi, private val cityMapper: CityMapper) :
    WeatherRepository {
    override suspend fun getWeather(cityName: String): CityWeather =
        cityMapper.map(api.getWeather(cityName))

    override suspend fun getWeatherNearLocation(coordinates: Coordinates): List<CityWeather> =
        api.getWeatherNearLocation(
            coordinates.latitude,
            coordinates.longitude
        ).list.map { city -> cityMapper.map(city) }

    override suspend fun getWeather(cityId: Int): CityWeather =
        cityMapper.map(api.getWeather(cityId))
}
