package com.example.weatherApp.domain.usecase

import com.example.weatherApp.domain.WeatherRepository
import com.example.weatherApp.domain.entities.CityWeather
import com.example.weatherApp.domain.entities.Coordinates
import com.example.weatherApp.utils.MainCoroutineRule
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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetWeatherNearUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository
    lateinit var getWeatherNearUseCase: GetWeatherNearUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcher = MainCoroutineRule().testDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        getWeatherNearUseCase = GetWeatherNearUseCase(weatherRepository, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Test if we get right weather near location")
    fun invokeTest() = runTest(dispatcher) {
        // arrange
        val expectedValue = Coordinates("1", "1")
        val expectedCityId = 1
        val expectedCityName = "City"
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { id } returns expectedCityId
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
        }
        coEvery { weatherRepository.getWeatherNearLocation(expectedValue) } returns listOf(
            mockWeather
        )
        // act
        val result = getWeatherNearUseCase(expectedValue)
        // assert
        assertEquals(
            expectedCityId,
            result.first().id
        )
        assertEquals(
            expectedCityName,
            result.first().name
        )
        assertEquals(
            expectedCityTemp,
            result.first().temp
        )
    }
}