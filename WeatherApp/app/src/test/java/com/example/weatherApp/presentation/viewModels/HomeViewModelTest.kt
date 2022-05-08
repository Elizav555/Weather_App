package com.example.weatherApp.presentation.viewModels

import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.domain.usecase.GetWeatherNearUseCase
import com.example.weatherApp.domain.usecase.GetWeatherUseCase
import com.example.weatherApp.utils.MainCoroutineRule
import getOrAwaitValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class HomeViewModelTest {
    private val cityName = "City"
    private val coordinates = Coordinates("1", "1")

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcher = MainCoroutineRule().testDispatcher

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @MockK
    lateinit var getWeatherNearUseCase: GetWeatherNearUseCase
    private lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        homeViewModel = HomeViewModel(getWeatherUseCase, getWeatherNearUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherByName() {
        val expectedCityName = "City"
        val expectedCityId = 1
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
            every { id } returns expectedCityId

        }
        coEvery { getWeatherUseCase(cityName) } returns mockWeather
        runTest(dispatcher) {
            homeViewModel.getWeather(cityName)
            assertEquals(
                Result.success(mockWeather),
                homeViewModel.weather.getOrAwaitValue()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNearWeather() = runTest(dispatcher) {
        val expectedCityName = "City"
        val expectedCityId = 1
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
            every { id } returns expectedCityId

        }
        coEvery { getWeatherNearUseCase(coordinates) } returns listOf(mockWeather)
        homeViewModel.getNearWeather(coordinates)
        assertEquals(
            Result.success(listOf(mockWeather)),
            homeViewModel.weather.getOrAwaitValue()
        )
    }
}