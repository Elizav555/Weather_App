package com.example.weatherApp.presentation.viewModels

import dagger.assisted.AssistedFactory

@AssistedFactory
interface CityViewModelAssistedFactory {
    fun create(id: Int): CityViewModel
}
