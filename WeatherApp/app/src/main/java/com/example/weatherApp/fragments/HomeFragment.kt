package com.example.weatherApp.fragments

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherApp.R
import com.example.weatherApp.city.CityAdapter
import com.example.weatherApp.data.WeatherRepository
import com.example.weatherApp.data.response.City
import com.example.weatherApp.data.response.Coord
import com.example.weatherApp.databinding.FragmentHomeBinding
import com.example.weatherApp.utils.autoCleared
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var rationaleDialog: AlertDialog? = null
    private var cityAdapter: CityAdapter by autoCleared()
    private var cities = listOf<City>()
    private val defaultCoord = Coord(55.7867758, 49.15536300000001)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureSearch()
        binding.progressBar.visibility = View.VISIBLE
        checkLocationPermission()
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

    override fun onDestroy() {
        super.onDestroy()
        rationaleDialog?.dismiss()
        rationaleDialog = null
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else getCitiesForList(defaultCoord)
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

    private fun navigateToCity(city: City) {
        val action = HomeFragmentDirections.actionHomeFragmentToCityFragment(city.id)
        findNavController().navigate(action)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        LocationServices.getFusedLocationProviderClient(requireActivity())
            .lastLocation.addOnSuccessListener { location ->
                location?.let {
                    return@addOnSuccessListener getCitiesForList(
                        Coord(
                            it.latitude,
                            it.longitude
                        )
                    )
                }
                getCitiesForList(defaultCoord)
            }.addOnFailureListener {
                getCitiesForList(defaultCoord)
            }
    }

    private fun getCitiesForList(coord: Coord) {
        runBlocking {
            cities = WeatherRepository().getWeatherNearLocation(coord)
            init()
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getCity(cityName: String): Boolean {
        var city: City?
        try {
            runBlocking {
                city = WeatherRepository().getWeather(cityName)
            }
            city?.let {
                navigateToCity(it)
                return true
            }
        } catch (er: Exception) {
            Snackbar.make(
                binding.root,
                "Invalid city",
                Snackbar.LENGTH_LONG
            ).show()
        }
        return false
    }

    private fun configureSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (validateQuery(query)) {
                    return getCity(query)
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
}
