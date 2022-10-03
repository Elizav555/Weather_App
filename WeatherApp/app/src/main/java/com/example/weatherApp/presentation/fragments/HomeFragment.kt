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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.weatherApp.R
import com.example.weatherApp.databinding.FragmentHomeBinding
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.domain.utils.ColorManager
import com.example.weatherApp.presentation.city.CityAdapter
import com.example.weatherApp.presentation.utils.autoCleared
import com.example.weatherApp.presentation.viewModels.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var rationaleDialog: AlertDialog? = null
    private var cityAdapter: CityAdapter by autoCleared()
    private var cities = listOf<CityWeather>()
    private val defaultCoordinates = Coordinates("35.652832", "139.839478")
    private var lastSearchTime: Long = Calendar.getInstance().timeInMillis

    @Inject
    lateinit var colorManager: ColorManager

    @Inject
    lateinit var client: FusedLocationProviderClient
    private val homeViewModel: HomeViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                getCitiesForList(defaultCoordinates)
                showSnackbar(getString(R.string.no_location_access))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            .setMessage(getString(R.string.rationale_msg))
            .setPositiveButton(getString(R.string.grant_access)) { _, _ ->
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun init() {
        val navigateAction = { transitionView: View, cityId: Int ->
            navigateToCity(
                transitionView,
                cityId
            )
        }
        cityAdapter = CityAdapter(navigateAction, cities, colorManager)
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

    private fun navigateToCity(transitionView: View, cityId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToCityFragment(cityId)
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
                    showSnackbar(getString(R.string.error_location))
                }
            }
        }
    }

    private fun getCitiesForList(coordinates: Coordinates) {
        homeViewModel.getNearWeather(coordinates)
    }

    private suspend fun getCity(cityName: String) = homeViewModel.getWeather(cityName)


    private fun configureSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val actualSearchTime: Long = Calendar.getInstance().timeInMillis
                if (actualSearchTime > lastSearchTime + 100) {
                    lastSearchTime = actualSearchTime

                    if (validateQuery(query)) {
                        lifecycleScope.launch {
                            getCity(query)?.let { navigateToCity(it) }
                                ?: showSnackbar(getString(R.string.invalid_city))
                        }
                    }
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
