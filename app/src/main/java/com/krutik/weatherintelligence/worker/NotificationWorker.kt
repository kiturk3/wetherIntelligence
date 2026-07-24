package com.krutik.weatherintelligence.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.domain.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: WeatherRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val lat = inputData.getDouble("lat", 28.6139)
            val lon = inputData.getDouble("lon", 77.2090)

            val weatherResource = repository.getCurrentWeather(lat, lon, forceRefresh = false).firstOrNull { it !is Resource.Loading }
            val weather = weatherResource?.data

            if (weather != null) {
                val condition = weather.condition.lowercase()
                val isSevere = condition.contains("thunderstorm") ||
                        condition.contains("rain") ||
                        condition.contains("snow") ||
                        condition.contains("squall") ||
                        condition.contains("tornado") ||
                        weather.temperature > 35.0 ||
                        weather.temperature < 0.0 ||
                        weather.windSpeed > 10.0

                if (isSevere) {
                    showNotification(
                        title = "Severe Weather Alert - ${weather.cityName}",
                        message = "Current condition: ${weather.condition}, Temp: ${weather.temperature}°C, Wind: ${weather.windSpeed} m/s"
                    )
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "weather_alerts"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Severe Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for severe weather alerts and extreme conditions"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}
