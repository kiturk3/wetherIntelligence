package com.krutik.weatherintelligence.domain.usecase

import com.krutik.weatherintelligence.core.common.Resource
import com.krutik.weatherintelligence.fakes.FakeWeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCurrentWeatherUseCaseTest {

    private lateinit var repository: FakeWeatherRepository
    private lateinit var useCase: GetCurrentWeatherUseCase

    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        useCase = GetCurrentWeatherUseCase(repository)
    }

    @Test
    fun `invoke should return success weather resource from repository`() = runTest {
        val result = useCase(28.6, 77.2).first()

        assertTrue(result is Resource.Success)
        assertEquals("New Delhi", (result as Resource.Success).data?.cityName)
    }

    @Test
    fun `invoke with error repository should return error resource`() = runTest {
        repository.shouldReturnError = true
        val result = useCase(28.6, 77.2).first()

        assertTrue(result is Resource.Error)
        assertEquals("Failed to fetch weather", (result as Resource.Error).message)
    }
}
