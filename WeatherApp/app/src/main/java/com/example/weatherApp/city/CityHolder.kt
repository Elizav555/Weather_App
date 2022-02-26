package com.example.weatherApp.city

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherApp.R
import com.example.weatherApp.data.response.City
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.utils.ColorManager
import kotlin.math.roundToInt

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

    fun bind(city: City) {
        with(binding) {
            val res: Resources = context.resources
            tempTv.setTextColor(ColorManager().chooseTempColor(city.main.temp))
            tempTv.text = res.getString(R.string.temp, city.main.temp.roundToInt())
            cityTv.text = city.name
        }
    }
}
