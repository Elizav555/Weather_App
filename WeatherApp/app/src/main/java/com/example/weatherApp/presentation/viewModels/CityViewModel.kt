package com.example.weatherApp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityViewModel @Inject constructor(
    private var getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private var _weather: MutableLiveData<Result<CityWeather>> = MutableLiveData()
    val weather: LiveData<Result<CityWeather>> = _weather

    fun getWeather(cityId: Int) {
        viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(cityId)
                _weather.value = Result.success(weather)
            } catch (ex: Exception) {
                _weather.value = Result.failure(ex)
            }
        }
    }
}
