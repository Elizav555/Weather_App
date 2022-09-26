package com.example.weatherApp.presentation.city

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherApp.R
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager

class CityHolder(
    private val binding: ItemCityBinding,
    private val context: Context,
    action: (transitionView: View, position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action.invoke(binding.cityNameTv, adapterPosition)
        }
    }

    fun bind(city: CityWeather) {
        with(binding) {
            val res: Resources = context.resources
            val color = ColorManager(context).chooseTempColor(city.temp)
            tempTv.setTextColor(color)
            tempTv.text = res.getString(R.string.temp, city.temp)
            cityNameTv.text = city.name
            this.city = city
            this.executePendingBindings()
        }
    }
}
