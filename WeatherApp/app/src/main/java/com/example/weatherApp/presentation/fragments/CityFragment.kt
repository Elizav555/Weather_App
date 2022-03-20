package com.example.weatherApp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.weatherApp.R
import com.example.weatherApp.databinding.FragmentCityBinding
import com.example.weatherApp.di.DIContainer
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager
import com.example.weatherApp.presentation.utils.ViewModelFactory
import com.example.weatherApp.presentation.viewModels.CityViewModel

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    lateinit var dataBinding: FragmentCityBinding
    private val args: CityFragmentArgs by navArgs()
    private lateinit var viewModel: CityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObjects()
        initObservers()
        binding = FragmentCityBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding = FragmentCityBinding.bind(view)
        dataBinding.isLoading = true
        viewModel.getWeather(args.cityId)
    }

    private fun bindWeatherInfo(city: CityWeather) {
        dataBinding.city = city
        dataBinding.colorManager = ColorManager(requireContext())
        dataBinding.iconUrl = getString(R.string.weather_icon, city.weatherIcon)
        dataBinding.isLoading = false
//            with(binding) {
//            curtempTv.setTextColor(
//                ColorManager().chooseTempColor(
//                    city.temp,
//                    requireContext()
//                )
//            )
//            curtempTv.text = getString(R.string.temp, city.temp)
//            cityNameTv.text = city.name


//            weatherIv.load(iconUri) {
//                error(R.drawable.weather)
//                listener(
//                    onError = { _: Any?, throwable: Throwable ->
//                        throwable.message?.let {
//                            Log.println(Log.ERROR, "coil", it)
//                        }
//                    },
//                )
//            }

//            descTv.text = city.weatherDesc
//            windTv.text = getString(R.string.wind, city.windDir, city.windSpeed)
//            feelsLikeTv.setTextColor(
//                ColorManager().chooseTempColor(
//                    city.feelsLikeTemp,
//                    requireContext()
//                )
//            )
//            feelsLikeTv.text = getString(R.string.feels_temp, city.feelsLikeTemp)
//            humidityTv.text = getString(R.string.humidity, city.humidity)
//            pressureTv.text = getString(R.string.pressure, city.pressure)
//
//            progressBar.visibility = View.GONE
//            cityFields.visibility = View.VISIBLE
//        }
    }

    private fun initObservers() {
        viewModel.weather.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val city = it
                bindWeatherInfo(city)
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }

    private fun initObjects() {
        val factory = ViewModelFactory(DIContainer)
        viewModel = ViewModelProvider(
            viewModelStore,
            factory
        )[CityViewModel::class.java]
    }
}
