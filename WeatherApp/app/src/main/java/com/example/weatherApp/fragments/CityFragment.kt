package com.example.weatherApp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.weatherApp.R
import com.example.weatherApp.data.WeatherRepository
import com.example.weatherApp.data.response.City
import com.example.weatherApp.databinding.FragmentCityBinding
import com.example.weatherApp.utils.ColorManager
import com.example.weatherApp.utils.DirectionManager
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private val args: CityFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val city = WeatherRepository().getWeather(args.cityId)
            bindWeatherInfo(city)
        }
    }

    private fun bindWeatherInfo(city: City) {
        with(binding) {
            curtempTv.setTextColor(
                ColorManager().chooseTempColor(
                    city.main.temp,
                    requireContext()
                )
            )
            curtempTv.text = getString(R.string.temp, city.main.temp.roundToInt())
            cityNameTv.text = city.name

            val iconUri = getString(R.string.weather_icon, city.weather[0].icon)
            weatherIv.load(iconUri) {
                error(R.drawable.weather)
                listener(
                    onError = { _: Any?, throwable: Throwable ->
                        throwable.message?.let {
                            Log.println(Log.ERROR, "coil", it)
                        }
                    },
                )
            }
            val desc = city.weather[0].description.replaceFirstChar { it.uppercase() }
            descTv.text = desc

            val windDirection = DirectionManager().degreesToDirection(city.wind.deg)
            windTv.text =
                getString(R.string.wind, windDirection, "%.1f".format(city.wind.speed))

            feelsLikeTv.setTextColor(
                ColorManager().chooseTempColor(
                    city.main.feels_like,
                    requireContext()
                )
            )
            feelsLikeTv.text = getString(R.string.feels_temp, city.main.feels_like.roundToInt())
            humidityTv.text = getString(R.string.humidity, city.main.humidity)
            pressureTv.text = getString(R.string.pressure, city.main.pressure)

            progressBar.visibility = View.GONE
            cityFields.visibility = View.VISIBLE
        }
    }
}
