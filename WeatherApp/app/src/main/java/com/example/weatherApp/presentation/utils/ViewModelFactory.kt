package com.example.weatherApp.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import com.example.weatherApp.presentation.viewModels.CityViewModel
import com.example.weatherApp.presentation.viewModels.HomeViewModel

class ViewModelFactory(
    val getWeatherUseCase: GetWeatherUseCase,
    val getWeatherNearUseCase: GetWeatherNearUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(getWeatherUseCase, getWeatherNearUseCase) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")

            modelClass.isAssignableFrom(CityViewModel::class.java) ->
                CityViewModel(getWeatherUseCase) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")

            else ->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
