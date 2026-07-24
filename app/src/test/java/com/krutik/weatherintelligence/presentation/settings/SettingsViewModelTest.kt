package com.krutik.weatherintelligence.presentation.settings

import app.cash.turbine.test
import com.krutik.weatherintelligence.fakes.FakeSettingsRepository
import com.krutik.weatherintelligence.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeSettingsRepository
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        repository = FakeSettingsRepository()
        viewModel = SettingsViewModel(repository)
    }

    @Test
    fun `toggleTempUnit should update tempUnit StateFlow`() = runTest {
        viewModel.tempUnit.test {
            assertEquals("metric", awaitItem())

            viewModel.toggleTempUnit("imperial")
            assertEquals("imperial", awaitItem())
        }
    }
}
