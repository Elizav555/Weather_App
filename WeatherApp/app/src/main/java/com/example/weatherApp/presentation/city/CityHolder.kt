package com.example.weatherApp.presentation.city

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherApp.R
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager

class CityHolder(
    private val binding: ItemCityBinding,
    private val context: Context,
    action: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(city: CityWeather) {
        with(binding) {
            val res: Resources = context.resources
            val color = ColorManager().chooseTempColor(city.temp, context)
            tempTv.setTextColor(color)
            tempTv.text = res.getString(R.string.temp, city.temp)
            cityTv.text = city.name
        }
    }
}
