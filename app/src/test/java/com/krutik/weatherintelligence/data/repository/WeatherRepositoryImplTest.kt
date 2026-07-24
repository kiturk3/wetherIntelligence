package com.krutik.weatherintelligence.data.repository

import com.krutik.weatherintelligence.data.local.WeatherDatabase
import com.krutik.weatherintelligence.data.local.dao.CacheMetadataDao
import com.krutik.weatherintelligence.data.local.dao.CityDao
import com.krutik.weatherintelligence.data.local.dao.ForecastDao
import com.krutik.weatherintelligence.data.local.dao.WeatherDao
import com.krutik.weatherintelligence.data.local.entity.CacheMetadataEntity
import com.krutik.weatherintelligence.data.local.entity.WeatherEntity
import com.krutik.weatherintelligence.data.remote.datasource.RemoteDataSource
import com.krutik.weatherintelligence.data.remote.dto.CurrentWeatherResponseDto
import com.krutik.weatherintelligence.data.remote.dto.ForecastResponseDto
import com.krutik.weatherintelligence.data.remote.dto.MainDataDto
import com.krutik.weatherintelligence.data.remote.dto.WeatherConditionDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private val remoteDataSource: RemoteDataSource = mockk(relaxed = true)
    private val db: WeatherDatabase = mockk(relaxed = true)
    private val weatherDao: WeatherDao = mockk(relaxed = true)
    private val forecastDao: ForecastDao = mockk(relaxed = true)
    private val cityDao: CityDao = mockk(relaxed = true)
    private val cacheMetadataDao: CacheMetadataDao = mockk(relaxed = true)

    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        every { db.weatherDao() } returns weatherDao
        every { db.forecastDao() } returns forecastDao
        every { db.cityDao() } returns cityDao
        every { db.cacheMetadataDao() } returns cacheMetadataDao

        repository = WeatherRepositoryImpl(remoteDataSource, db)
    }

    @Test
    fun `getCurrentWeather with expired cache should trigger remote fetch for current weather and forecast`() = runTest {
        // Given expired cache metadata
        coEvery { cacheMetadataDao.getCacheMetadata("CURRENT_WEATHER") } returns CacheMetadataEntity(
            cacheKey = "CURRENT_WEATHER",
            ttlMs = 900000L,
            updatedAt = 1000L,
            expiresAt = 2000L
        )

        val mockWeatherEntity = WeatherEntity(
            id = 1,
            cityName = "London",
            lat = 51.5,
            lon = -0.1,
            temperature = 18.0,
            feelsLike = 17.0,
            condition = "Clouds",
            icon = "03d",
            humidity = 70,
            windSpeed = 5.0,
            pressure = 1015,
            visibility = 10000,
            uvIndex = 3.0,
            sunrise = 1600000000L,
            sunset = 1600040000L,
            updatedAt = System.currentTimeMillis()
        )
        every { weatherDao.getCurrentWeather() } returns flowOf(mockWeatherEntity)

        val mockCurrentDto = CurrentWeatherResponseDto(
            dt = System.currentTimeMillis() / 1000,
            name = "London",
            main = MainDataDto(temp = 18.0, feelsLike = 17.0, pressure = 1015, humidity = 70),
            weather = listOf(WeatherConditionDto(802, "Clouds", "scattered clouds", "03d"))
        )
        val mockForecastDto = ForecastResponseDto(list = emptyList())

        coEvery { remoteDataSource.getCurrentWeather(any(), any(), any(), any()) } returns mockCurrentDto
        coEvery { remoteDataSource.getForecast(any(), any(), any(), any()) } returns mockForecastDto

        // When
        val result = repository.getCurrentWeather(51.5, -0.1, forceRefresh = false).first()

        // Then
        coVerify { remoteDataSource.getCurrentWeather(51.5, -0.1, "metric", any()) }
        coVerify { remoteDataSource.getForecast(51.5, -0.1, "metric", any()) }
        assertEquals("London", result.data?.cityName)
    }
}
