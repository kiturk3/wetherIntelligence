package com.krutik.weatherintelligence.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_items")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "HOURLY" or "DAILY"
    val timeEpoch: Long,
    val temp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val icon: String,
    val pop: Double
)
