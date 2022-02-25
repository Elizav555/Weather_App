package com.example.weatherApp.city

import androidx.recyclerview.widget.RecyclerView
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.data.response.City

class CityHolder(
    private val binding: ItemCityBinding,
    action: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(city: City) {
        with(binding) {
            tempTv.text = city.main.temp.toString()
            cityTv.text = city.name
        }
    }
}
