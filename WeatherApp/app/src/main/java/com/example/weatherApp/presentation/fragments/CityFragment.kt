package com.example.weatherApp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.weatherApp.R
import com.example.weatherApp.databinding.FragmentCityBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.utils.ColorManager
import com.example.weatherApp.presentation.viewModels.CityViewModel
import com.example.weatherApp.presentation.viewModels.CityViewModelAssistedFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityFragment : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private val args: CityFragmentArgs by navArgs()

    @Inject
    lateinit var assistedFactory: CityViewModelAssistedFactory

    private val cityViewModel: CityViewModel by viewModels {
        CityViewModel.Factory(assistedFactory, args.cityId)
    }

    @Inject
    lateinit var colorManager: ColorManager
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
        initObservers()
        val transition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        binding.executePendingBindings()
        binding.isLoading = true
        cityViewModel.getWeather()
    }

    private fun bindWeatherInfo(city: CityWeather) {
        binding.city = city
        binding.colorManager = colorManager
        binding.iconUrl = getString(R.string.weather_icon, city.weatherIcon)
        binding.isLoading = false
    }

    private fun initObservers() {
        cityViewModel.weather.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val city = it
                bindWeatherInfo(city)
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
