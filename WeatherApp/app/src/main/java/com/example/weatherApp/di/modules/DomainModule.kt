package com.example.weatherApp.di.modules

import com.example.weatherApp.domain.WeatherRepository
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class DomainModule {
    @Provides
    fun provideGetWeatherUseCase(weatherRepository: WeatherRepository) = GetWeatherUseCase(
        weatherRepository = weatherRepository,
        dispatcher = Dispatchers.Default
    )

    @Provides
    fun provideGetWeatherNearUseCase(weatherRepository: WeatherRepository) = GetWeatherNearUseCase(
        weatherRepository = weatherRepository,
        dispatcher = Dispatchers.Default
    )
}
