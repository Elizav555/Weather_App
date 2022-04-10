package com.example.weatherApp.presentation.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager

class CityAdapter(
    private val action: (transitionView: View, cityId: Int) -> Unit,
    private val cities: List<CityWeather>,
    private val colorManager: ColorManager
) :
    ListAdapter<CityWeather, CityHolder>(CityDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityHolder = CityHolder(
        ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action,
        colorManager
    )

    override fun onBindViewHolder(
        holder: CityHolder,
        position: Int
    ) {
        val city = cities[position]
        holder.bind(city)
    }

    override fun submitList(list: List<CityWeather>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
