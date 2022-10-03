package com.example.weatherApp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var getWeatherUseCase: GetWeatherUseCase,
    private var getWeatherNearUseCase: GetWeatherNearUseCase,
) : ViewModel() {
    private var _weatherList: MutableLiveData<Result<List<CityWeather>>> = MutableLiveData()
    val weatherList: LiveData<Result<List<CityWeather>>> = _weatherList

    suspend fun getWeather(cityName: String) = runCatching {
        getWeatherUseCase(cityName)
    }.getOrNull()

    fun getNearWeather(coordinates: Coordinates) {
        viewModelScope.launch {
            try {
                val weatherList = getWeatherNearUseCase(coordinates)
                _weatherList.value = Result.success(weatherList)
            } catch (ex: Exception) {
                _weatherList.value = Result.failure(ex)
            }
        }
    }
}
