package com.krutik.weatherintelligence.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krutik.weatherintelligence.presentation.theme.WeatherIntelligenceTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_rendersAppTitleAndButtons() {
        composeTestRule.setContent {
            WeatherIntelligenceTheme {
                HomeScreen(
                    onNavigateToSearch = {},
                    onNavigateToDetails = {},
                    onNavigateToSettings = {}
                )
            }
        }

        composeTestRule.onNodeWithText("🌤️ Weather Intelligence").assertIsDisplayed()
    }
}
