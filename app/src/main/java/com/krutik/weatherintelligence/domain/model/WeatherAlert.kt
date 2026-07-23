package com.krutik.weatherintelligence.domain.model

data class WeatherAlert(
    val senderName: String,
    val event: String,
    val startEpoch: Long,
    val endEpoch: Long,
    val description: String
)
