package com.krutik.weatherintelligence.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.core.location.LocationTracker
import com.krutik.weatherintelligence.domain.model.CurrentWeather
import com.krutik.weatherintelligence.domain.model.DailyForecast
import com.krutik.weatherintelligence.domain.model.HourlyForecast
import com.krutik.weatherintelligence.domain.usecase.GetCurrentWeatherUseCase
import com.krutik.weatherintelligence.domain.usecase.GetDailyForecastUseCase
import com.krutik.weatherintelligence.domain.usecase.GetHourlyForecastUseCase
import com.krutik.weatherintelligence.worker.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val currentWeather: CurrentWeather? = null,
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val dailyForecast: List<DailyForecast> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    @Inject
    lateinit var syncManager: SyncManager
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        onEvent(HomeEvent.FetchCurrentLocationWeather)
        syncManager.schedulePeriodicSync()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RefreshWeather -> fetchWeatherData(event.lat, event.lon, forceRefresh = true)
            is HomeEvent.SelectCity -> fetchWeatherData(event.lat, event.lon, forceRefresh = false)
            is HomeEvent.FetchCurrentLocationWeather -> fetchCurrentLocationWeather()
        }
    }

    private fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                fetchWeatherData(location.latitude, location.longitude, forceRefresh = true)
            } else {
                // Fallback coordinates (New Delhi) if location permission not granted or GPS unavailable
                fetchWeatherData(28.6139, 77.2090, forceRefresh = false)
            }
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            getCurrentWeatherUseCase(lat, lon, forceRefresh).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                    is Resource.Success -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentWeather = result.data,
                        error = null
                    )
                    is Resource.Error -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}
