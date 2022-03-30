package com.example.weatherApp.presentation.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.weatherApp.R
import com.example.weatherApp.databinding.FragmentHomeBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.presentation.App
import com.example.weatherApp.presentation.city.CityAdapter
import com.example.weatherApp.presentation.utils.ViewModelFactory
import com.example.weatherApp.presentation.utils.autoCleared
import com.example.weatherApp.presentation.viewModels.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import javax.inject.Inject


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var rationaleDialog: AlertDialog? = null
    private var cityAdapter: CityAdapter by autoCleared()
    private var cities = listOf<CityWeather>()
    private val defaultCoordinates = Coordinates("35.652832", "139.839478")

    @Inject
    lateinit var client: FusedLocationProviderClient

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        homeViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[HomeViewModel::class.java]

        binding = FragmentHomeBinding.inflate(inflater)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        configureSearch()

        postponeEnterTransition()
        binding.recyclerView.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
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
        val navigateAction = { transitionView: View, position: Int ->
            navigateToCity(
                transitionView,
                cities[position]
            )
        }
        cityAdapter = CityAdapter(navigateAction, cities)
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

    private fun navigateToCity(transitionView: View, city: CityWeather) {
        val action = HomeFragmentDirections.actionHomeFragmentToCityFragment(city.id)
        findNavController().navigate(
            action,
            FragmentNavigator.Extras.Builder()
                .addSharedElements(
                    mapOf(transitionView to transitionView.transitionName)
                ).build()
        )
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
            client.lastLocation.addOnCompleteListener {
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
        homeViewModel.getNearWeather(coordinates)
    }

    private fun getCity(cityName: String) {
        homeViewModel.getWeather(cityName)
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

    private fun initObservers() {
        homeViewModel.weather.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val city = it
                navigateToCity(city)
            }, onFailure = {
                if (it is HttpException)
                    showSnackbar("Invalid city")
                Log.e("asd", it.message.toString())
            })
        }

        homeViewModel.weatherList.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                cities = it
                init()
                binding.progressBar.visibility = View.GONE
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }

    private fun showSnackbar(text: String) = Snackbar.make(
        binding.root,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}
