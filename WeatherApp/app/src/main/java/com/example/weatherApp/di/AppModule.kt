package com.example.weatherApp.di

import android.app.Application
import android.content.Context
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import com.example.weatherApp.presentation.utils.ViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    fun provideViewModelFactory(
        getWeatherUseCase: GetWeatherUseCase,
        getWeatherNearUseCase: GetWeatherNearUseCase
    ) = ViewModelFactory(getWeatherUseCase, getWeatherNearUseCase)

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
