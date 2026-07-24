package com.krutik.weatherintelligence.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.core.location.LocationTracker
import com.krutik.weatherintelligence.domain.model.CurrentWeather
import com.krutik.weatherintelligence.domain.usecase.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherDetailsUiState(
    val isLoading: Boolean = false,
    val weather: CurrentWeather? = null,
    val error: String? = null
)

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherDetailsUiState())
    val uiState: StateFlow<WeatherDetailsUiState> = _uiState.asStateFlow()

    init {
        loadWeatherDetails()
    }

    fun loadWeatherDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val location = locationTracker.getCurrentLocation()
            val lat = location?.latitude ?: 28.6139
            val lon = location?.longitude ?: 77.2090

            getCurrentWeatherUseCase(lat, lon, forceRefresh = false).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                    is Resource.Success -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weather = result.data,
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
