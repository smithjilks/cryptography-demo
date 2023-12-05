package com.smithjilks.cryptographydemo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smithjilks.cryptographydemo.ui.screens.AppScreens
import com.smithjilks.cryptographydemo.ui.screens.auth.AuthScreen
import com.smithjilks.cryptographydemo.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.AuthScreen.name
    ) {

        composable(AppScreens.AuthScreen.name) {
            AuthScreen(navController = navController)
        }

        composable(AppScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
    }

}