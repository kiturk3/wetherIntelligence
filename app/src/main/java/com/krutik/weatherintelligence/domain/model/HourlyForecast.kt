package com.krutik.weatherintelligence.domain.model

data class HourlyForecast(
    val timeEpoch: Long,
    val temp: Double,
    val condition: String,
    val icon: String,
    val pop: Double // Probability of precipitation
)
