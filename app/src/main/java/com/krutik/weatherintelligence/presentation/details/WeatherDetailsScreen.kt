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
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krutik.weatherintelligence.presentation.components.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    onNavigateBack: () -> Unit
) {
    GradientBackground(condition = "Clear") {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Weather Details & Metrics", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Air Quality Card
                MetricCard(
                    title = "Air Quality Index (AQI)",
                    value = "Good (42)",
                    icon = Icons.Rounded.Air,
                    progress = 0.25f,
                    progressColor = Color(0xFF4CAF50),
                    description = "Air quality is satisfactory and poses little or no risk."
                )

                // UV Index Gauge Card
                MetricCard(
                    title = "UV Index Gauge",
                    value = "5 - Moderate",
                    icon = Icons.Rounded.WbSunny,
                    progress = 0.5f,
                    progressColor = Color(0xFFFFB300),
                    description = "Wear sunglasses on bright days; use SPF 30+ sunscreen."
                )

                // Wind Speed & Direction Card
                MetricCard(
                    title = "Wind & Gusts",
                    value = "14 km/h • Direction: NW",
                    icon = Icons.Rounded.Explore,
                    progress = 0.35f,
                    progressColor = Color(0xFF29B6F6),
                    description = "Gentle breeze moving north-west with gusts up to 22 km/h."
                )

                // Barometric Pressure Card
                MetricCard(
                    title = "Barometric Pressure",
                    value = "1013 hPa",
                    icon = Icons.Rounded.Compress,
                    progress = 0.65f,
                    progressColor = Color(0xFFAB47BC),
                    description = "Standard atmospheric sea-level pressure."
                )
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
