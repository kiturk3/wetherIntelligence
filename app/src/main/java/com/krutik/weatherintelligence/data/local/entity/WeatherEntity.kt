package com.krutik.weatherintelligence.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class WeatherEntity(
    @PrimaryKey val id: Int = 1,
    val cityName: String,
    val lat: Double,
    val lon: Double,
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
    val updatedAt: Long
)
