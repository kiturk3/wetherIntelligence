package com.krutik.weatherintelligence.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: WeatherRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val lat = inputData.getDouble("lat", 28.6139)
        val lon = inputData.getDouble("lon", 77.2090)

        val targetLat = if (lat == 0.0) 28.6139 else lat
        val targetLon = if (lon == 0.0) 77.2090 else lon

        return when (repository.refreshWeather(targetLat, targetLon)) {
            is Resource.Success -> Result.success()
            is Resource.Error -> Result.retry()
            is Resource.Loading -> Result.retry()
        }
    }
}
