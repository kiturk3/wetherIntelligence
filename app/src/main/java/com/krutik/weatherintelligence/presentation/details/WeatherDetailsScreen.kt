package com.krutik.weatherintelligence.presentation.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Compress
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krutik.weatherintelligence.presentation.components.GradientBackground
import com.krutik.weatherintelligence.presentation.components.LoadingView
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: WeatherDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val weather = state.weather
    val condition = weather?.condition ?: "Clear"

    GradientBackground(condition = condition) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(if (weather != null) "Metrics • ${weather.cityName}" else "Weather Details & Metrics", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            if (state.isLoading && weather == null) {
                LoadingView(message = "Loading detailed metrics...")
            } else if (weather != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val uvVal = weather.uvIndex
                    val uvProgress = (uvVal / 11.0).toFloat().coerceIn(0f, 1f)
                    val uvCategory = when {
                        uvVal <= 2.0 -> "Low"
                        uvVal <= 5.0 -> "Moderate"
                        uvVal <= 7.0 -> "High"
                        uvVal <= 10.0 -> "Very High"
                        else -> "Extreme"
                    }
                    val uvDesc = when {
                        uvVal <= 2.0 -> "No protection needed. You can safely stay outside."
                        uvVal <= 5.0 -> "Wear sunglasses and SPF 30+ sunscreen."
                        else -> "Seek shade during midday hours; wear hat and UV sunglasses."
                    }

                    // UV Index Gauge Card
                    MetricCard(
                        title = "UV Index Gauge",
                        value = "${uvVal.roundToInt()} - $uvCategory",
                        icon = Icons.Rounded.WbSunny,
                        progress = uvProgress,
                        progressColor = Color(0xFFFFB300),
                        description = uvDesc
                    )

                    // Wind Speed Card
                    val windKmh = (weather.windSpeed * 3.6).roundToInt()
                    val windProgress = (windKmh / 60f).coerceIn(0f, 1f)
                    MetricCard(
                        title = "Wind Speed & Gusts",
                        value = "$windKmh km/h (${weather.windSpeed} m/s)",
                        icon = Icons.Rounded.Explore,
                        progress = windProgress,
                        progressColor = Color(0xFF29B6F6),
                        description = "Current wind speed recorded for ${weather.cityName}."
                    )

                    // Humidity Card
                    val humProgress = (weather.humidity / 100f).coerceIn(0f, 1f)
                    val dewPoint = (weather.temperature - ((100 - weather.humidity) / 5)).roundToInt()
                    MetricCard(
                        title = "Humidity & Dew Point",
                        value = "${weather.humidity}% • Dew Point ~${dewPoint}°C",
                        icon = Icons.Rounded.WaterDrop,
                        progress = humProgress,
                        progressColor = Color(0xFF42A5F5),
                        description = if (weather.humidity > 70) "High humidity makes it feel warmer." else "Comfortable relative humidity."
                    )

                    // Barometric Pressure Card
                    val pressProgress = ((weather.pressure - 950) / 100f).coerceIn(0f, 1f)
                    MetricCard(
                        title = "Barometric Pressure",
                        value = "${weather.pressure} hPa",
                        icon = Icons.Rounded.Compress,
                        progress = pressProgress,
                        progressColor = Color(0xFFAB47BC),
                        description = if (weather.pressure >= 1013) "High pressure brings clear skies." else "Low pressure indicates possible clouds/precipitation."
                    )

                    // Visibility Card
                    val visKm = weather.visibility / 1000f
                    val visProgress = (visKm / 10f).coerceIn(0f, 1f)
                    MetricCard(
                        title = "Atmospheric Visibility",
                        value = "${"%.1f".format(visKm)} km",
                        icon = Icons.Rounded.Air,
                        progress = visProgress,
                        progressColor = Color(0xFF4CAF50),
                        description = if (visKm >= 10f) "Clear visual distance." else "Reduced visibility due to haze/clouds."
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    progress: Float,
    progressColor: Color,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.padding(end = 8.dp))
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = progressColor,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)))
        }
    }
}
