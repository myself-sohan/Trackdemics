package com.example.trackdemics.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trackdemics.screens.role.RoleScreen
import com.example.trackdemics.screens.home.StudentHomeScreen
import com.example.trackdemics.screens.login.LoginScreen
import com.example.trackdemics.screens.signup.SignUpScreen
import androidx.navigation.NavType
import com.example.trackdemics.screens.home.AdminHomeScreen
import com.example.trackdemics.screens.home.ProfessorHomeScreen

@Composable
fun TrackdemicsNavigation()
{
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination = TrackdemicsScreens.SignUpScreen.name
    )
    {
        composable(TrackdemicsScreens.StudentHomeScreen.name)
        {
            StudentHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.AdminHomeScreen.name)
        {
            AdminHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.ProfessorHomeScreen.name)
        {
            ProfessorHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.RoleScreen.name)
        {
            RoleScreen(navController = navController)
        }
        val loginRoute = "${TrackdemicsScreens.LoginScreen.name}/{role}"
        composable(
            route = loginRoute,
            arguments = listOf(
                navArgument("role")
                {
                    type = NavType.StringType
                }
            )
        )
        { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role").orEmpty()
            LoginScreen(
                navController = navController,
                role = role
            )
        }

        val signUpRoute = "${TrackdemicsScreens.SignUpScreen.name}/{role}"
        composable(
            route = signUpRoute,
            arguments = listOf(
                navArgument("role")
                {
                    type = NavType.StringType
                }
            )
        )
        { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role").orEmpty()
            SignUpScreen(
                navController = navController,
                role = role
            )
        }
    }
}