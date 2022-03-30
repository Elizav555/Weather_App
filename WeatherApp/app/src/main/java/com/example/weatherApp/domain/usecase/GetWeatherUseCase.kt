package com.example.weatherApp.domain.usecase

import com.example.weatherApp.domain.WeatherRepository
import com.example.weatherApp.domain.entities.CityWeather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(cityName: String): CityWeather {
        return withContext(dispatcher) {
            weatherRepository.getWeather(cityName)
        }
    }

    suspend operator fun invoke(cityId: Int): CityWeather {
        return withContext(dispatcher) {
            weatherRepository.getWeather(cityId)
        }
    }
}
