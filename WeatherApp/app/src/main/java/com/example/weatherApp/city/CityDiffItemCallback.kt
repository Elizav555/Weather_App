package com.example.weatherApp.city

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherApp.data.response.City

class CityDiffItemCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean = oldItem == newItem
}
