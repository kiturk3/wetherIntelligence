package com.krutik.weatherintelligence.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krutik.weatherintelligence.data.local.dao.WeatherDao
import com.krutik.weatherintelligence.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        weatherDao = database.weatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetWeather_shouldReturnInsertedWeatherEntity() = runTest {
        val weather = WeatherEntity(
            id = 1,
            cityName = "Tokyo",
            lat = 35.6762,
            lon = 139.6503,
            temperature = 22.5,
            feelsLike = 21.0,
            condition = "Clear",
            icon = "01d",
            humidity = 40,
            windSpeed = 3.5,
            pressure = 1010,
            visibility = 10000,
            uvIndex = 5.0,
            sunrise = 1600000000L,
            sunset = 1600040000L,
            updatedAt = System.currentTimeMillis()
        )

        weatherDao.insertWeather(weather)
        val result = weatherDao.getCurrentWeather().first()

        assertNotNull(result)
        assertEquals("Tokyo", result?.cityName)
        assertEquals(22.5, result?.temperature ?: 0.0, 0.01)
    }
}
