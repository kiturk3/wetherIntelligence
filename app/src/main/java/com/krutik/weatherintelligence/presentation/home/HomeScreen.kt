package com.krutik.weatherintelligence.presentation.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krutik.weatherintelligence.domain.model.DailyForecast
import com.krutik.weatherintelligence.domain.model.HourlyForecast
import com.krutik.weatherintelligence.presentation.components.DailyForecastSection
import com.krutik.weatherintelligence.presentation.components.ErrorView
import com.krutik.weatherintelligence.presentation.components.GradientBackground
import com.krutik.weatherintelligence.presentation.components.HourlyForecastRow
import com.krutik.weatherintelligence.presentation.components.LoadingView
import com.krutik.weatherintelligence.presentation.components.TemperatureChartCard
import com.krutik.weatherintelligence.presentation.components.WeatherCard
import com.krutik.weatherintelligence.presentation.components.WeatherDetailsGrid

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val condition = state.currentWeather?.condition ?: "Clear"

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.onEvent(HomeEvent.FetchCurrentLocationWeather)
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    GradientBackground(condition = condition) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌤️ Weather Intelligence",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row {
                        IconButton(onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                            viewModel.onEvent(HomeEvent.FetchCurrentLocationWeather)
                        }) {
                            Icon(Icons.Rounded.MyLocation, contentDescription = "My Location", tint = Color.White)
                        }
                        IconButton(onClick = onNavigateToSearch) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.White)
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                    }
                }

                if (state.isLoading && state.currentWeather == null) {
                    LoadingView()
                } else if (state.error != null && state.currentWeather == null) {
                    ErrorView(
                        message = state.error!!,
                        onRetry = { viewModel.onEvent(HomeEvent.FetchCurrentLocationWeather) }
                    )
                } else if (state.currentWeather != null) {
                    // Hero Weather Card
                    WeatherCard(weather = state.currentWeather!!)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Hourly Forecast Horizontal List
                    val mockHourly = if (state.hourlyForecast.isNotEmpty()) state.hourlyForecast else getMockHourlyForecasts()
                    HourlyForecastRow(hourlyForecasts = mockHourly)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Temperature Trend Canvas Chart
                    TemperatureChartCard(hourlyForecasts = mockHourly)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Weather Metrics Grid (Humidity, Pressure, Wind, UV Index, Sunrise/Sunset)
                    WeatherDetailsGrid(weather = state.currentWeather!!)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 7-Day Forecast
                    val mockDaily = if (state.dailyForecast.isNotEmpty()) state.dailyForecast else getMockDailyForecasts()
                    DailyForecastSection(dailyForecasts = mockDaily)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// Fallback preview mocks when network is offline/empty
private fun getMockHourlyForecasts(): List<HourlyForecast> {
    val now = System.currentTimeMillis() / 1000
    return listOf(
        HourlyForecast(now, 28.0, "Sunny", "01d", 0.0),
        HourlyForecast(now + 3600, 29.0, "Sunny", "01d", 0.0),
        HourlyForecast(now + 7200, 31.0, "Sunny", "01d", 0.0),
        HourlyForecast(now + 10800, 30.0, "Cloudy", "02d", 0.2),
        HourlyForecast(now + 14400, 28.0, "Rain", "10d", 0.6),
        HourlyForecast(now + 18000, 26.0, "Rain", "10d", 0.8),
        HourlyForecast(now + 21600, 25.0, "Cloudy", "03d", 0.1),
        HourlyForecast(now + 25200, 24.0, "Clear", "01n", 0.0)
    )
}

private fun getMockDailyForecasts(): List<DailyForecast> {
    val now = System.currentTimeMillis() / 1000
    val daySec = 86400L
    return listOf(
        DailyForecast(now, 22.0, 32.0, "Sunny", "01d", 0.0),
        DailyForecast(now + daySec, 23.0, 31.0, "Cloudy", "02d", 0.2),
        DailyForecast(now + daySec * 2, 21.0, 29.0, "Rain", "10d", 0.7),
        DailyForecast(now + daySec * 3, 20.0, 28.0, "Thunderstorm", "11d", 0.9),
        DailyForecast(now + daySec * 4, 22.0, 30.0, "Cloudy", "03d", 0.1),
        DailyForecast(now + daySec * 5, 23.0, 33.0, "Sunny", "01d", 0.0),
        DailyForecast(now + daySec * 6, 24.0, 34.0, "Sunny", "01d", 0.0)
    )
}
