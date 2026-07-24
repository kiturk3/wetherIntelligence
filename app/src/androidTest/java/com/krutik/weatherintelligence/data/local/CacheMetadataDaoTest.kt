package com.krutik.weatherintelligence.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krutik.weatherintelligence.data.local.dao.CacheMetadataDao
import com.krutik.weatherintelligence.data.local.entity.CacheMetadataEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheMetadataDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var cacheMetadataDao: CacheMetadataDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        cacheMetadataDao = database.cacheMetadataDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetMetadata_shouldReturnInsertedCacheMetadata() = runTest {
        val metadata = CacheMetadataEntity(
            cacheKey = "CURRENT_WEATHER",
            ttlMs = 900000L,
            updatedAt = 1000L,
            expiresAt = 901000L
        )

        cacheMetadataDao.insertCacheMetadata(metadata)
        val result = cacheMetadataDao.getCacheMetadata("CURRENT_WEATHER")

        assertNotNull(result)
        assertEquals("CURRENT_WEATHER", result?.cacheKey)
        assertEquals(901000L, result?.expiresAt)
    }
}
