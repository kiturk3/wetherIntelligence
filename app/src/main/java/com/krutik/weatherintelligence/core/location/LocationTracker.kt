package com.krutik.weatherintelligence.core.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}
