package com.example.weatherApp.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherApp.di.DIContainer
import com.example.weatherApp.presentation.CityViewModel
import com.example.weatherApp.presentation.HomeViewModel

class ViewModelFactory(
    private val di: DIContainer,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(di.getWeatherUseCase, di.getWeatherNearUseCase) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")

            modelClass.isAssignableFrom(CityViewModel::class.java) ->
                CityViewModel(di.getWeatherUseCase) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")

            else ->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
