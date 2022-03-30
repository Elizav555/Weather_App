package com.example.weatherApp.di

import android.content.Context
import com.example.weatherApp.domain.WeatherRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, DomainModule::class])
interface AppComponent {

    fun getContext(): Context

    fun getWeatherRepository(): WeatherRepository
}
