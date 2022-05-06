package com.example.weatherApp.presentation.viewModels


import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.weatherApp.domain.entities.CityWeather
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
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(InstantExecutorExtension::class, MockKExtension::class)
internal class CityViewModelTest {
    private val cityId = 1

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val dispatcher = MainCoroutineRule().testDispatcher

    @MockK
    lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var cityViewModel: CityViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        cityViewModel = CityViewModel(cityId, getWeatherUseCase, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather() = runBlocking {
        val expectedCityName = "City"
        val expectedCityTemp = 10
        val mockWeather = mockk<CityWeather>() {
            every { name } returns expectedCityName
            every { temp } returns expectedCityTemp
        }
        coEvery { getWeatherUseCase(cityId) } returns mockWeather
        cityViewModel.getWeather()
        assertEquals(
            Result.success(mockWeather),
            cityViewModel.weather.getOrAwaitValue()
        )
    }

    @Test
    fun getWeatherException() {
    }
}

class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

}