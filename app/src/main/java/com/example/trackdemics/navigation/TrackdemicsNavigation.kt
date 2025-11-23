package com.example.trackdemics.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trackdemics.screens.attendance.AdminAttendanceScreen
import com.example.trackdemics.screens.attendance.CourseAttendanceScreen
import com.example.trackdemics.screens.attendance.EditAttendanceScreen
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
import com.example.trackdemics.screens.transport.BusScheduleScreen
import com.example.trackdemics.screens.transport.SeatBookingScreen
import com.example.trackdemics.screens.transport.SpecialBusScreen
import com.example.trackdemics.widgets.ConfirmExitOnBack

@SuppressLint("ContextCastToActivity")
@Composable
fun TrackdemicsNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TrackdemicsScreens.SplashScreen.name
    ) {

        // Screens without arguments
        composable(TrackdemicsScreens.AdminAttendanceScreen.name) {
            AdminAttendanceScreen(navController = navController)
        }
        composable(TrackdemicsScreens.RoutineScreen.name) {
            RoutineScreen(navController = navController)
        }
        composable(TrackdemicsScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(TrackdemicsScreens.ProfessorAttendanceScreen.name) {
            ProfessorAttendanceScreen(navController = navController)
        }
        composable(TrackdemicsScreens.StudentHomeScreen.name) {
            val activity = LocalContext.current as Activity
            ConfirmExitOnBack(activity = activity)
            StudentHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.StudentAttendanceScreen.name) {
            StudentAttendanceScreen(navController = navController)
        }
        composable(TrackdemicsScreens.AdminHomeScreen.name) {
            val activity = LocalContext.current as Activity
            ConfirmExitOnBack(activity = activity)
            AdminHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.ProfessorHomeScreen.name) {
            val activity = LocalContext.current as Activity
            ConfirmExitOnBack(activity = activity)
            ProfessorHomeScreen(navController = navController)
        }
        composable(TrackdemicsScreens.RoleScreen.name) {
            val activity = LocalContext.current as Activity
            ConfirmExitOnBack(activity = activity)
            RoleScreen(navController = navController)
        }

        // Screens with code & name arguments
        // --- SeatBooking route with full bus arguments ---
        val seatBookingRoute =
            "${TrackdemicsScreens.SeatBookingScreen.name}/{id}/{dateTime}/{route}/{busCode}/{seatsTaken}/{capacity}/{isBooked}/{role}"
        composable(
            route = seatBookingRoute,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("dateTime") { type = NavType.StringType },
                navArgument("route") { type = NavType.StringType },
                navArgument("busCode") { type = NavType.StringType },
                navArgument("seatsTaken") { type = NavType.IntType },
                navArgument("capacity") { type = NavType.IntType },
                navArgument("isBooked") { type = NavType.BoolType },
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val dateTime = backStackEntry.arguments?.getString("dateTime").orEmpty()
            val routeArg = backStackEntry.arguments?.getString("route").orEmpty()
            val busCode = backStackEntry.arguments?.getString("busCode").orEmpty()
            val seatsTaken = backStackEntry.arguments?.getInt("seatsTaken") ?: 0
            val capacity = backStackEntry.arguments?.getInt("capacity") ?: 0
            val isBooked = backStackEntry.arguments?.getBoolean("isBooked") ?: false
            val role = backStackEntry.arguments?.getString("role").orEmpty()

            SeatBookingScreen(
                navController = navController,
                busId = id,
                dateTime = dateTime,
                route = routeArg,
                busCode = busCode,
                seatsTaken = seatsTaken,
                capacity = capacity,
                isBooked = isBooked,
                role = role
            )
        }

        // --- SpecialBusScreen route to receive booking result updates: (bookedBusId + updatedSeats + role again) ---
        val specialBusResultRoute1 =
            "${TrackdemicsScreens.SpecialBusScreen.name}/{bookedBusId}/{updatedSeats}/{role}"
        composable(
            route = specialBusResultRoute1,
            arguments = listOf(
                navArgument("bookedBusId") { type = NavType.IntType },
                navArgument("updatedSeats") { type = NavType.IntType },
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookedBusId = backStackEntry.arguments?.getInt("bookedBusId")
            val updatedSeats = backStackEntry.arguments?.getInt("updatedSeats")
            val role = backStackEntry.arguments?.getString("role").orEmpty()
            SpecialBusScreen(
                navController = navController,
                bookedBusId = bookedBusId,
                updatedSeats = updatedSeats,
                role = role
            )
        }
        // --- SpecialBusScreen: keep the original no-arg route (for normal entry) ---
        val specialBusResultRoute2 = "${TrackdemicsScreens.SpecialBusScreen.name}/{role}"
        composable(
            route = specialBusResultRoute2,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role").orEmpty()
            SpecialBusScreen(navController = navController, role = role)
        }
        val busScheduleRoute2 =
            "${TrackdemicsScreens.BusScheduleScreen.name}/{bookedBusId}/{updatedSeats}/{role}"
        composable(
            route = busScheduleRoute2,
            arguments = listOf(
                navArgument("bookedBusId") { type = NavType.IntType },
                navArgument("updatedSeats") { type = NavType.IntType },
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookedBusId = backStackEntry.arguments?.getInt("bookedBusId")
            val updatedSeats = backStackEntry.arguments?.getInt("updatedSeats")
            val role = backStackEntry.arguments?.getString("role").orEmpty()
            BusScheduleScreen(
                navController = navController,
                bookedBusId = bookedBusId,
                updatedSeats = updatedSeats,
                role = role
            )
        }
        // Bus Schedule Screen
        val busScheduleRoute1 = "${TrackdemicsScreens.BusScheduleScreen.name}/{role}"
        composable(
            route = busScheduleRoute1,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role").orEmpty()
            BusScheduleScreen(
                navController = navController,
                role = role,
            )
        }

        val courseAttendanceRoute =
            "${TrackdemicsScreens.CourseAttendanceScreen.name}/{code}/{name}"
        composable(
            route = courseAttendanceRoute,
            arguments = listOf(
                navArgument("code") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            CourseAttendanceScreen(
                navController = navController,
                courseCode = code,
                courseName = name
            )
        }

        val studentListRoute = "${TrackdemicsScreens.StudentListScreen.name}/{code}/{name}"
        composable(
            route = studentListRoute,
            arguments = listOf(
                navArgument("code") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            StudentListScreen(
                navController = navController,
                code = code,
                name = name
            )
        }
        val editAttendanceRoute = "${TrackdemicsScreens.EditAttendanceScreen.name}/{code}/{name}"
        composable(
            route = editAttendanceRoute,
            arguments = listOf(
                navArgument("code") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            EditAttendanceScreen(
                navController = navController,
                courseCode = code,
                courseName = name
            )
        }

        // Screens with role argument
        val loginRoute = "${TrackdemicsScreens.LoginScreen.name}/{role}"
        composable(
            route = loginRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
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
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
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
