package com.example.weatherApp.presentation.city

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherApp.R
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager
import com.example.weatherApp.presentation.App

class CityHolder(
    private val binding: ItemCityBinding,
    private val action: (transitionView: View, cityId: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(city: CityWeather) {
        with(binding) {
            root.setOnClickListener {
                action.invoke(binding.tvCityName, city.id)
            }
            val res: Resources = App.appComponent.getContext().resources
            val color = ColorManager().chooseTempColor(city.temp)
            tvTemp.setTextColor(color)
            tvTemp.text = res.getString(R.string.temp, city.temp)
            tvCityName.text = city.name
            this.city = city
            this.executePendingBindings()
        }
    }
}
