package com.example.weatherApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.weatherApp.R
import com.example.weatherApp.data.WeatherRepository
import com.example.weatherApp.databinding.FragmentCityBinding
import com.example.weatherApp.utils.ColorManager
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private val args: CityFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCityBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // show loading
        lifecycleScope.launch {
            val city = WeatherRepository().getWeather(args.cityId)
            with(binding) {
                curtempTv.setTextColor(ColorManager().chooseTempColor(city.main.temp))
                curtempTv.text = getString(R.string.temp, city.main.temp.roundToInt())
                cityNameTv.text = city.name
                val iconUrl = getString(R.string.weather_icon, city.weather[0].icon)
                weatherIv.load(iconUrl)
                descTv.text = city.weather[0].description
                windTv.text = city.wind.deg.toString() // need to convert degrees to direction
                feelsLikeTv.setTextColor(ColorManager().chooseTempColor(city.main.feels_like))
                feelsLikeTv.text = getString(R.string.feels_temp, city.main.feels_like.roundToInt())
                humidityTv.text = city.main.humidity.toString()
                pressureTv.text = city.main.pressure.toString()
            }
        }
    }
}
