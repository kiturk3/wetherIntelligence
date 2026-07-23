package com.krutik.weatherintelligence.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krutik.weatherintelligence.core.utils.DateTimeFormatter
import com.krutik.weatherintelligence.domain.model.HourlyForecast

@Composable
fun HourlyForecastRow(
    hourlyForecasts: List<HourlyForecast>,
    unit: String = "metric",
    modifier: Modifier = Modifier
) {
    val tempUnitSymbol = if (unit == "metric") "°C" else "°F"

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Hourly Forecast",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(hourlyForecasts) { item ->
                HourlyItemCard(item = item, tempUnitSymbol = tempUnitSymbol)
            }
        }
    }
}

@Composable
fun HourlyItemCard(
    item: HourlyForecast,
    tempUnitSymbol: String
) {
    Card(
        modifier = Modifier.width(76.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = DateTimeFormatter.formatEpochToTime(item.timeEpoch),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            WeatherIcon(
                condition = item.condition,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (item.pop > 0.1) {
                Text(
                    text = "${(item.pop * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF81D4FA),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "${item.temp.toInt()}$tempUnitSymbol",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
