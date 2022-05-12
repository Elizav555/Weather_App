package com.example.weatherApp.presentation.viewModels

import com.example.weatherApp.domain.entities.CityWeather
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
internal class CityViewModelTest {
    private val cityId = 1
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val dispatcher = MainCoroutineRule().testDispatcher

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var cityViewModel: CityViewModel

    @BeforeEach
    fun setUp() {
        cityViewModel = CityViewModel(cityId, getWeatherUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather() = runTest {
        val expectedCityName = "City"
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
        }
        coEvery { getWeatherUseCase(cityId) } returns mockWeather
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        try {
            cityViewModel.getWeather()
            assertEquals(
                Result.success(mockWeather),
                cityViewModel.weather.getOrAwaitValue()
            )
        } finally {
            Dispatchers.resetMain()
        }
    }
}
