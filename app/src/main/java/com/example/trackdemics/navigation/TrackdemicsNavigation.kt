package com.example.trackdemics.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trackdemics.screens.login.LoginScreen

@Composable
fun TrackdemicsNavigation()
{
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination = TrackdemicsScreens.LoginScreen.name
    )
    {
        composable(TrackdemicsScreens.LoginScreen.name)
        {
            LoginScreen(navController = navController)
        }
    }
}