package com.example.weatherApp.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherApp.data.response.City
import com.example.weatherApp.databinding.ItemCityBinding
import com.example.weatherApp.fragments.HomeFragment

class CityAdapter(
    private val action: (position: Int) -> Unit,
    private val cities: List<City>
) :
    ListAdapter<City, CityHolder>(CityDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityHolder = CityHolder(
        ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action
    )

    override fun onBindViewHolder(
        holder: CityHolder,
        position: Int
    ) {
        val city = cities[position]
        holder.bind(city)
    }

    override fun submitList(list: List<City>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
