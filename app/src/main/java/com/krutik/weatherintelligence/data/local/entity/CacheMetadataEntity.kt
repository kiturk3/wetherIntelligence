package com.krutik.weatherintelligence.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_metadata")
data class CacheMetadataEntity(
    @PrimaryKey val cacheKey: String, // e.g. "CURRENT_WEATHER", "FORECAST"
    val ttlMs: Long,
    val updatedAt: Long,
    val expiresAt: Long
)
