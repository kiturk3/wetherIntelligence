package com.krutik.weatherintelligence.presentation.details

import app.cash.turbine.test
import com.krutik.weatherintelligence.core.location.LocationTracker
import com.krutik.weatherintelligence.domain.usecase.GetCurrentWeatherUseCase
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import com.krutik.weatherintelligence.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeWeatherRepository
    private val locationTracker: LocationTracker = mockk(relaxed = true)
    private lateinit var viewModel: WeatherDetailsViewModel

    @Before
    fun setUp() {
        coEvery { locationTracker.getCurrentLocation() } returns null

        repository = FakeWeatherRepository()
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(repository)

        viewModel = WeatherDetailsViewModel(
            getCurrentWeatherUseCase = getCurrentWeatherUseCase,
            locationTracker = locationTracker
        )
    }

    @Test
    fun `initialization loads detailed weather metrics successfully`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.weather)
            assertEquals("New Delhi", state.weather?.cityName)
            assertEquals(28.0, state.weather?.temperature ?: 0.0, 0.1)
        }
    }
}
