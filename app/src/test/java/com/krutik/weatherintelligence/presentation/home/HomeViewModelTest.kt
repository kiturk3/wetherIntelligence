package com.krutik.weatherintelligence.presentation.home

import app.cash.turbine.test
import com.krutik.weatherintelligence.domain.usecase.GetCurrentWeatherUseCase
import com.krutik.weatherintelligence.domain.usecase.GetDailyForecastUseCase
import com.krutik.weatherintelligence.domain.usecase.GetHourlyForecastUseCase
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import com.krutik.weatherintelligence.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeWeatherRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(repository)
        val getHourlyForecastUseCase = GetHourlyForecastUseCase(repository)
        val getDailyForecastUseCase = GetDailyForecastUseCase(repository)

        viewModel = HomeViewModel(
            getCurrentWeatherUseCase,
            getHourlyForecastUseCase,
            getDailyForecastUseCase
        )
    }

    @Test
    fun `initialization should fetch weather and update uiState with success`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.currentWeather)
            assertEquals("New Delhi", state.currentWeather?.cityName)
        }
    }

    @Test
    fun `RefreshWeather event should trigger fetch and update uiState`() = runTest {
        viewModel.onEvent(HomeEvent.RefreshWeather(28.6139, 77.2090))

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("New Delhi", state.currentWeather?.cityName)
        }
    }
}
