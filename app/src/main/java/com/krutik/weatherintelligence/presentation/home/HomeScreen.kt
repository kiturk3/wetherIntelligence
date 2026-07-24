package com.krutik.weatherintelligence.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateToSearch) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.White)
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                    }
                }

                // Offline Mode Banner
                if (state.isOffline) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x33FFFFFF)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CloudOff,
                                contentDescription = "Offline",
                                tint = Color.White,
                                modifier = Modifier.height(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Offline - Displaying cached data",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if (state.isLoading && state.currentWeather == null) {
                    LoadingView()
                } else if (state.error != null && state.currentWeather == null) {
                    ErrorView(
                        message = state.error!!,
                        onRetry = { viewModel.fetchWeatherData(28.6139, 77.2090) }
                    )
                } else if (state.currentWeather != null) {
                    // Hero Weather Card
                    WeatherCard(weather = state.currentWeather!!)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Hourly Forecast Horizontal List
                    val hourlyData = if (state.hourlyForecast.isNotEmpty()) state.hourlyForecast else getMockHourlyForecasts()
                    HourlyForecastRow(hourlyForecasts = hourlyData)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Temperature Trend Canvas Chart
                    TemperatureChartCard(hourlyForecasts = hourlyData)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Weather Metrics Grid (Humidity, Pressure, Wind, UV Index, Sunrise/Sunset)
                    WeatherDetailsGrid(weather = state.currentWeather!!)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 7-Day Forecast
                    val dailyData = if (state.dailyForecast.isNotEmpty()) state.dailyForecast else getMockDailyForecasts()
                    DailyForecastSection(dailyForecasts = dailyData)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// Fallback preview mocks when network is offline and DB is empty
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
