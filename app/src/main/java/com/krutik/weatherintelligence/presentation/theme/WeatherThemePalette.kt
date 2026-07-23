package com.krutik.weatherintelligence.presentation.theme

import androidx.compose.ui.graphics.Color

enum class WeatherConditionType {
    SUNNY, CLOUDY, RAIN, STORM, NIGHT, SNOW
}

data class WeatherThemeColors(
    val primary: Color,
    val secondary: Color,
    val surface: Color,
    val onSurface: Color,
    val gradientColors: List<Color>
)

object WeatherThemePalettes {
    val Sunny = WeatherThemeColors(
        primary = Color(0xFFFFB300), // Amber
        secondary = Color(0xFF0288D1), // Sky Blue
        surface = Color(0x33FFFFFF), // Glassmorphism translucent white
        onSurface = Color(0xFF1E1E1E),
        gradientColors = listOf(
            Color(0xFFFFE082),
            Color(0xFFFFB74D),
            Color(0xFF81D4FA)
        )
    )

    val Cloudy = WeatherThemeColors(
        primary = Color(0xFF78909C), // Slate Gray
        secondary = Color(0xFF90A4AE), // Blue Gray
        surface = Color(0x33FFFFFF),
        onSurface = Color(0xFF263238),
        gradientColors = listOf(
            Color(0xFFB0BEC5),
            Color(0xFF78909C),
            Color(0xFF546E7A)
        )
    )

    val Rain = WeatherThemeColors(
        primary = Color(0xFF1565C0), // Deep Blue
        secondary = Color(0xFF00897B), // Teal
        surface = Color(0x33FFFFFF),
        onSurface = Color(0xFFFFFFFF),
        gradientColors = listOf(
            Color(0xFF1E88E5),
            Color(0xFF1565C0),
            Color(0xFF0D47A1)
        )
    )

    val Storm = WeatherThemeColors(
        primary = Color(0xFF673AB7), // Deep Purple
        secondary = Color(0xFF37474F), // Dark Gray
        surface = Color(0x33FFFFFF),
        onSurface = Color(0xFFFFFFFF),
        gradientColors = listOf(
            Color(0xFF4A148C),
            Color(0xFF311B92),
            Color(0xFF121212)
        )
    )

    val Night = WeatherThemeColors(
        primary = Color(0xFF7E57C2), // Deep Purple
        secondary = Color(0xFF1A237E), // Dark Blue
        surface = Color(0x22FFFFFF),
        onSurface = Color(0xFFE8EAF6),
        gradientColors = listOf(
            Color(0xFF1A237E),
            Color(0xFF121212),
            Color(0xFF000000)
        )
    )

    val Snow = WeatherThemeColors(
        primary = Color(0xFF00ACC1), // Cyan
        secondary = Color(0xFFB2EBF2), // Ice Blue
        surface = Color(0x44FFFFFF),
        onSurface = Color(0xFF102A43),
        gradientColors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB2EBF2),
            Color(0xFF80DEEA)
        )
    )

    fun getPaletteForCondition(condition: String, isNight: Boolean = false): WeatherThemeColors {
        if (isNight) return Night
        return when {
            condition.contains("Sun", ignoreCase = true) || condition.contains("Clear", ignoreCase = true) -> Sunny
            condition.contains("Cloud", ignoreCase = true) || condition.contains("Overcast", ignoreCase = true) -> Cloudy
            condition.contains("Rain", ignoreCase = true) || condition.contains("Drizzle", ignoreCase = true) -> Rain
            condition.contains("Thunder", ignoreCase = true) || condition.contains("Storm", ignoreCase = true) -> Storm
            condition.contains("Snow", ignoreCase = true) || condition.contains("Ice", ignoreCase = true) -> Snow
            else -> Sunny
        }
    }
}
