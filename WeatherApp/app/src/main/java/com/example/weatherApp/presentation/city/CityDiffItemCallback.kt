package com.example.weatherApp.presentation.city

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherApp.domain.entities.CityWeather

class CityDiffItemCallback : DiffUtil.ItemCallback<CityWeather>() {
    override fun areItemsTheSame(
        oldItem: CityWeather,
        newItem: CityWeather
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CityWeather,
        newItem: CityWeather
    ): Boolean = oldItem == newItem
}
