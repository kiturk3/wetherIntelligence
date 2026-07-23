package com.krutik.weatherintelligence.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherIcon(
    condition: String,
    isNight: Boolean = false,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    val icon = when {
        isNight -> Icons.Rounded.NightsStay
        condition.contains("Sun", ignoreCase = true) || condition.contains("Clear", ignoreCase = true) -> Icons.Rounded.WbSunny
        condition.contains("Cloud", ignoreCase = true) || condition.contains("Overcast", ignoreCase = true) -> Icons.Rounded.Cloud
        condition.contains("Rain", ignoreCase = true) || condition.contains("Drizzle", ignoreCase = true) -> Icons.Rounded.Grain
        condition.contains("Thunder", ignoreCase = true) || condition.contains("Storm", ignoreCase = true) -> Icons.Rounded.Thunderstorm
        else -> Icons.Rounded.WbSunny
    }

    val iconTint = if (tint != Color.Unspecified) tint else when {
        isNight -> Color(0xFFE0E0E0)
        condition.contains("Sun", ignoreCase = true) || condition.contains("Clear", ignoreCase = true) -> Color(0xFFFFB300)
        condition.contains("Cloud", ignoreCase = true) -> Color(0xFF90A4AE)
        condition.contains("Rain", ignoreCase = true) -> Color(0xFF29B6F6)
        condition.contains("Thunder", ignoreCase = true) -> Color(0xFFAB47BC)
        else -> Color(0xFFFFB300)
    }

    Icon(
        imageVector = icon,
        contentDescription = contentDescription ?: condition,
        modifier = modifier,
        tint = iconTint
    )
}
