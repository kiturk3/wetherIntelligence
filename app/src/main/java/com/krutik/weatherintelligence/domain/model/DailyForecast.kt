package com.krutik.weatherintelligence.domain.model

data class DailyForecast(
    val dayEpoch: Long,
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val icon: String,
    val pop: Double
)
