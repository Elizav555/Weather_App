package com.example.weatherApp.domain.usecase

import com.example.weatherApp.domain.WeatherRepository
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWeatherNearUseCase(
    private val weatherRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    suspend operator fun invoke(coordinates: Coordinates): List<CityWeather> {
        return withContext(dispatcher) {
            weatherRepository.getWeatherNearLocation(coordinates)
        }
    }
}
