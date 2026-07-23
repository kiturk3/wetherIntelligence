package com.krutik.weatherintelligence.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeFormatter {
    fun formatEpochToTime(epochSeconds: Long): String {
        val date = Date(epochSeconds * 1000)
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(date)
    }

    fun formatEpochToDay(epochSeconds: Long): String {
        val date = Date(epochSeconds * 1000)
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(date)
    }
}
