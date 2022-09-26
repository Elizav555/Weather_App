package com.example.weatherApp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.weatherApp.R
import com.example.weatherApp.databinding.FragmentCityBinding
import com.example.weatherApp.di.DIContainer
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager
import com.example.weatherApp.presentation.utils.ViewModelFactory
import com.example.weatherApp.presentation.viewModels.CityViewModel

class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private val args: CityFragmentArgs by navArgs()
    private lateinit var viewModel: CityViewModel

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
        initObjects()
        initObservers()
        val transition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        binding.executePendingBindings()
        binding.isLoading = true
        viewModel.getWeather(args.cityId)
    }

    private fun bindWeatherInfo(city: CityWeather) {
        binding.city = city
        binding.colorManager = ColorManager(requireContext())
        binding.iconUrl = getString(R.string.weather_icon, city.weatherIcon)
        binding.isLoading = false
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
