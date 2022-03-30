package com.example.weatherApp.di.components

import android.content.Context
import com.example.weatherApp.di.modules.AppModule
import com.example.weatherApp.di.modules.DataModule
import com.example.weatherApp.domain.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {
    fun getFusedClient(): FusedLocationProviderClient

    fun getContext(): Context

    fun getWeatherRepository(): WeatherRepository
}
