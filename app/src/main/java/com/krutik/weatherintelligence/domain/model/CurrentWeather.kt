package com.krutik.weatherintelligence.domain.model

data class CurrentWeather(
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val condition: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val visibility: Int,
    val uvIndex: Double,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long
)
