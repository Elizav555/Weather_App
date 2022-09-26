package com.example.weatherApp.presentation.viewModels

import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import com.example.weatherApp.utils.InstantExecutorExtension
import getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, InstantExecutorExtension::class)
internal class HomeViewModelTest {
    private val cityName = "City"
    private val coordinates = Coordinates("1", "1")
//
//    @get:Rule
//    val dispatcher = MainCoroutineRule() почему тут так не работает? из-за junit5?

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @MockK
    lateinit var getWeatherNearUseCase: GetWeatherNearUseCase
    private lateinit var homeViewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        homeViewModel = HomeViewModel(getWeatherUseCase, getWeatherNearUseCase)
    }

    @Test
    fun getWeatherByName() = runTest {
        val expectedCityName = "City"
        val expectedCityId = 1
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
            every { id } returns expectedCityId

        }
        coEvery { getWeatherUseCase(cityName) } returns mockWeather
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        try {
            homeViewModel.getWeather(cityName)
            assertEquals(
                Result.success(mockWeather),
                homeViewModel.weather.getOrAwaitValue()
            )
        } finally {
            Dispatchers.resetMain()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNearWeather() = runTest {
        val expectedCityName = "City"
        val expectedCityId = 1
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
            every { id } returns expectedCityId

        }
        coEvery { getWeatherNearUseCase(coordinates) } returns listOf(mockWeather)
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        try {
            homeViewModel.getNearWeather(coordinates)
            assertEquals(
                Result.success(listOf(mockWeather)),
                homeViewModel.weatherList.getOrAwaitValue()
            )
        } finally {
            Dispatchers.resetMain()
        }
    }
}