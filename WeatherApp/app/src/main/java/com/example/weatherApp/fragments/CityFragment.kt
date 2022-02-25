package com.example.weatherApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.weatherApp.R
import com.example.weatherApp.data.WeatherRepository
import com.example.weatherApp.databinding.FragmentCityBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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
        MainScope().launch {
            val city = WeatherRepository().getWeather(args.cityName)
            with(binding) {
                cityNameTv.text = city.name
                curtempTv.text = city.main.temp.toString()
                weatherIv.load(getString(R.string.weather_icon, city.weather[0].icon))
                descTv.text = city.weather[0].description
                windTv.text = city.wind.deg.toString() // need to convert degrees to direction
                feelsLikeTv.text = city.main.feels_like.toString()
                humidityTv.text = city.main.humidity.toString()
                pressureTv.text = city.main.pressure.toString()
            }
        }
    }
}
