package com.example.weatherApp.presentation.viewModels

import androidx.lifecycle.*
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CityViewModel @AssistedInject constructor(
    @Assisted private val cityId: Int,
    private var getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private var _weather: MutableLiveData<Result<CityWeather>> = MutableLiveData()
    val weather: LiveData<Result<CityWeather>> = _weather

    fun getWeather() {
        viewModelScope.launch {
            try {
                val weather = getWeatherUseCase(cityId)
                _weather.value = Result.success(weather)
            } catch (ex: Exception) {
                _weather.value = Result.failure(ex)
            }
        }
    }

    class Factory(
        private val assistedFactory: CityViewModelAssistedFactory,
        private val id: Int,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return assistedFactory.create(id) as T
        }
    }
}
