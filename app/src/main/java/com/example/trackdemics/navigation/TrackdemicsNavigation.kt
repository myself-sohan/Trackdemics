package com.example.trackdemics.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trackdemics.screens.RoleScreen
import com.example.trackdemics.screens.login.LoginScreen
import com.example.trackdemics.screens.signup.SignUpScreen

@Composable
fun TrackdemicsNavigation()
{
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination = TrackdemicsScreens.RoleScreen.name
    )
    {
        composable(TrackdemicsScreens.RoleScreen.name)
        {
            RoleScreen(navController = navController)
        }
        composable(TrackdemicsScreens.LoginScreen.name)
        {
            LoginScreen(navController = navController)
        }
        composable(TrackdemicsScreens.SignUpScreen.name)
        {
            SignUpScreen(navController = navController)
        }
    }
}