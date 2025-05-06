package com.example.trackdemics.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trackdemics.screens.attendance.CourseAttendanceScreen
import com.example.trackdemics.screens.attendance.ProfessorAttendanceScreen
import com.example.trackdemics.screens.attendance.StudentAttendanceScreen
import com.example.trackdemics.screens.attendance.StudentListScreen
import com.example.trackdemics.screens.home.AdminHomeScreen
import com.example.trackdemics.screens.home.ProfessorHomeScreen
import com.example.trackdemics.screens.home.StudentHomeScreen
import com.example.trackdemics.screens.login.LoginScreen
import com.example.trackdemics.screens.role.RoleScreen
import com.example.trackdemics.screens.routine.RoutineScreen
import com.example.trackdemics.screens.signup.SignUpScreen
import com.example.trackdemics.screens.signup.SignUpViewModel
import com.example.trackdemics.screens.splash.SplashScreen

@Composable
fun TrackdemicsNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TrackdemicsScreens.SplashScreen.name
    )
    {
        composable(TrackdemicsScreens.StudentListScreen.name)
        {
            StudentListScreen(navController = navController)
        }
        composable(TrackdemicsScreens.CourseAttendanceScreen.name)
        {
            CourseAttendanceScreen(navController = navController)
        }
        composable(TrackdemicsScreens.RoutineScreen.name)
        {
            RoutineScreen(navController = navController)
        }
        composable(TrackdemicsScreens.SplashScreen.name)
        {
            SplashScreen(navController = navController)
        }
        composable(TrackdemicsScreens.ProfessorAttendanceScreen.name)
        {
            ProfessorAttendanceScreen(navController = navController)
        }
        composable(TrackdemicsScreens.StudentHomeScreen.name)
        {
            StudentHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.StudentAttendanceScreen.name)
        {
            StudentAttendanceScreen(navController = navController)
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
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(
                navController = navController,
                role = role,
                viewModel = viewModel
            )
        }
    }
}