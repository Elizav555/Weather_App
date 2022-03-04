package com.example.weatherApp.presentation.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherApp.R
import com.example.weatherApp.data.WeatherRepositoryImpl
import com.example.weatherApp.data.mapper.CityMapper
import com.example.weatherApp.databinding.FragmentHomeBinding
import com.example.weatherApp.di.DIContainer
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import com.example.weatherApp.presentation.autoCleared
import com.example.weatherApp.presentation.city.CityAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var rationaleDialog: AlertDialog? = null
    private var cityAdapter: CityAdapter by autoCleared()
    private var cities = listOf<CityWeather>()
    private val defaultCoordinates = Coordinates("35.652832", "139.839478")
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                getCitiesForList(defaultCoordinates)
                showSnackbar("No location access, set default")
            }
        }
    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var getWeatherNearUseCase: GetWeatherNearUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObjects()
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureSearch()
        binding.progressBar.visibility = View.VISIBLE
        checkLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        rationaleDialog?.dismiss()
        rationaleDialog = null
    }

    private fun checkLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ->
                getLocation()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->
                showLocationRationaleDialog()
            else ->
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
        }
    }

    private fun showLocationRationaleDialog() {
        rationaleDialog = AlertDialog.Builder(requireContext())
            .setMessage("Need access, so we can show cities nearby")
            .setPositiveButton("Grant access") { _, _ ->
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun init() {
        val navigateAction = { position: Int -> navigateToCity(cities[position]) }
        cityAdapter = CityAdapter(navigateAction, cities, requireContext())
        with(binding.recyclerView) {
            adapter = cityAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
        cityAdapter.submitList(cities)
    }

    private fun navigateToCity(city: CityWeather) {
        val action = HomeFragmentDirections.actionHomeFragmentToCityFragment(city.id)
        findNavController().navigate(action)
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(requireActivity())
                .lastLocation.addOnCompleteListener {
                    if (it.result != null) {
                        getCitiesForList(
                            Coordinates(
                                it.result.latitude.toString(),
                                it.result.longitude.toString()
                            )
                        )
                    } else {
                        getCitiesForList(defaultCoordinates)
                        showSnackbar("Error while getting your location, set default")
                    }
                }
        }
    }

    private fun getCitiesForList(coordinates: Coordinates) {
        lifecycleScope.launch {
            cities = getWeatherNearUseCase(coordinates)
            init()
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getCity(cityName: String) {
        lifecycleScope.launch {
            try {
                val city = getWeatherUseCase(cityName)
                navigateToCity(city)
            } catch (exc: HttpException) {
                showSnackbar("Invalid city")
            }
        }
    }

    private fun configureSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (validateQuery(query)) {
                    getCity(query)
                    return true
                }
                return false
            }

            private fun validateQuery(query: String): Boolean {
                return query.isNotBlank() && query.length > 2
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initObjects() {
        getWeatherUseCase = GetWeatherUseCase(
            weatherRepository = WeatherRepositoryImpl(
                api = DIContainer().api,
                cityMapper = CityMapper(),
            ),
            dispatcher = Dispatchers.Default
        )

        getWeatherNearUseCase = GetWeatherNearUseCase(
            weatherRepository = WeatherRepositoryImpl(
                api = DIContainer().api,
                cityMapper = CityMapper(),
            ),
            dispatcher = Dispatchers.Default
        )
    }

    private fun showSnackbar(text: String) = Snackbar.make(
        binding.root,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}