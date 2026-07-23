package com.krutik.weatherintelligence.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krutik.weatherintelligence.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecast_items WHERE type = :type ORDER BY timeEpoch ASC")
    fun getForecastByType(type: String): Flow<List<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecasts(forecasts: List<ForecastEntity>)

    @Query("DELETE FROM forecast_items WHERE type = :type")
    suspend fun clearForecastByType(type: String)
}
