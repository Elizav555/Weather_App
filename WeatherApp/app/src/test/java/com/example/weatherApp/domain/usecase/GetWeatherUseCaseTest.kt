package com.example.weatherApp.domain.usecase

import com.example.weatherApp.domain.WeatherRepository
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetWeatherUseCaseTest {
    @MockK
    lateinit var weatherRepository: WeatherRepository

    lateinit var getWeatherUseCase: GetWeatherUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcher = MainCoroutineRule().testDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        getWeatherUseCase = GetWeatherUseCase(weatherRepository, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Test if we get right weather by name")
    fun invokeTest() = runTest(dispatcher) {
        // arrange
        val expectedCityId = 1
        val expectedCityName = "City"
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { id } returns expectedCityId
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
        }
        coEvery {
            weatherRepository.getWeather(expectedCityName)
        } returns mockWeather

        // act
        val result = getWeatherUseCase(expectedCityName)
        // assert
        assertEquals(
            expectedCityId,
            result.id
        )
        assertEquals(
            expectedCityName,
            result.name
        )
        assertEquals(
            expectedCityTemp,
            result.temp
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Test if we get right weather by id")
    fun invokeTestSecond() = runTest(dispatcher) {
        // arrange
        val expectedCityId = 1
        val expectedCityName = "City"
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { id } returns expectedCityId
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
        }
        coEvery {
            weatherRepository.getWeather(expectedCityId)
        } returns mockWeather

        // act
        val result = getWeatherUseCase(expectedCityId)
        // assert
        assertEquals(
            expectedCityId,
            result.id
        )
        assertEquals(
            expectedCityName,
            result.name
        )
        assertEquals(
            expectedCityTemp,
            result.temp
        )
    }
}