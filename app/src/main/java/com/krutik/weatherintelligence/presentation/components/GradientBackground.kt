package com.krutik.weatherintelligence.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.krutik.weatherintelligence.presentation.theme.WeatherThemePalettes

@Composable
fun GradientBackground(
    condition: String = "Clear",
    isNight: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val palette = WeatherThemePalettes.getPaletteForCondition(condition, isNight)
    val color1 by animateColorAsState(targetValue = palette.gradientColors[0], animationSpec = tween(1000), label = "c1")
    val color2 by animateColorAsState(targetValue = palette.gradientColors[1], animationSpec = tween(1000), label = "c2")
    val color3 by animateColorAsState(targetValue = palette.gradientColors[2], animationSpec = tween(1000), label = "c3")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(color1, color2, color3)
                )
            ),
        content = content
    )
}
