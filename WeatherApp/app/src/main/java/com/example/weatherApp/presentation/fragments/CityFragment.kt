package com.example.weatherApp.presentation.fragments

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
import com.example.weatherApp.data.WeatherRepositoryImpl
import com.example.weatherApp.data.mapper.CityMapper
import com.example.weatherApp.databinding.FragmentCityBinding
import com.example.weatherApp.di.DIContainer
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import com.example.weatherApp.domain.utils.ColorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private val args: CityFragmentArgs by navArgs()
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObjects()
        binding = FragmentCityBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val city = getWeatherUseCase(args.cityId)
            bindWeatherInfo(city)
        }
    }

    private fun bindWeatherInfo(city: CityWeather) {
        with(binding) {
            curtempTv.setTextColor(
                ColorManager().chooseTempColor(
                    city.temp,
                    requireContext()
                )
            )
            curtempTv.text = getString(R.string.temp, city.temp)
            cityNameTv.text = city.name

            val iconUri = getString(R.string.weather_icon, city.weatherIcon)
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

            descTv.text = city.weatherDesc
            windTv.text = getString(R.string.wind, city.windDir, city.windSpeed)
            feelsLikeTv.setTextColor(
                ColorManager().chooseTempColor(
                    city.feelsLikeTemp,
                    requireContext()
                )
            )
            feelsLikeTv.text = getString(R.string.feels_temp, city.feelsLikeTemp)
            humidityTv.text = getString(R.string.humidity, city.humidity)
            pressureTv.text = getString(R.string.pressure, city.pressure)

            progressBar.visibility = View.GONE
            cityFields.visibility = View.VISIBLE
        }
    }

    private fun initObjects() {
        getWeatherUseCase = GetWeatherUseCase(
            weatherRepository = WeatherRepositoryImpl(
                api = DIContainer().api,
                cityMapper = CityMapper(),
            ),
            dispatcher = Dispatchers.Default
        )
    }
}
