package com.krutik.weatherintelligence.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.krutik.weatherintelligence.presentation.details.WeatherDetailsScreen
import com.krutik.weatherintelligence.presentation.home.HomeScreen
import com.krutik.weatherintelligence.presentation.search.SearchScreen
import com.krutik.weatherintelligence.presentation.settings.SettingsScreen
import com.krutik.weatherintelligence.presentation.splash.SplashScreen

@Composable
fun WeatherNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) { backStackEntry ->
            val selectedLat = backStackEntry.savedStateHandle.get<Double>("selected_lat")
            val selectedLon = backStackEntry.savedStateHandle.get<Double>("selected_lon")
            val selectedName = backStackEntry.savedStateHandle.get<String>("selected_name")

            HomeScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToDetails = { navController.navigate(Screen.Details.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                selectedCity = if (selectedLat != null && selectedLon != null) Triple(selectedLat, selectedLon, selectedName ?: "") else null,
                onCityConsumed = {
                    backStackEntry.savedStateHandle.remove<Double>("selected_lat")
                    backStackEntry.savedStateHandle.remove<Double>("selected_lon")
                    backStackEntry.savedStateHandle.remove<String>("selected_name")
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onCitySelected = { lat, lon, name ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_lat", lat)
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_lon", lon)
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_name", name)
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Details.route) {
            WeatherDetailsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
