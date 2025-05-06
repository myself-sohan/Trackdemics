package com.example.trackdemics.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun SplashScreen(
    navController: NavController
) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            try {
                // Get user role from Firestore
                val email = currentUser.email?.trim()?.lowercase()
                if (email != null) {
                    // Check if user exists in Students collection
                    val studentSnapshot = firestore.collection("students")
                        .whereEqualTo("email", email)
                        .get()
                        .await()

                    if (studentSnapshot.documents.isNotEmpty()) {
                        // User is a student
                        navController.navigate(TrackdemicsScreens.StudentHomeScreen.name) {
                            popUpTo(0) { inclusive = true } // Remove splash from back stack
                        }
                        return@LaunchedEffect
                    }

                    // Else, check professors
                    val professorSnapshot = firestore.collection("professors")
                        .whereEqualTo("email", email)
                        .get()
                        .await()

                    if (professorSnapshot.documents.isNotEmpty()) {
                        navController.navigate(TrackdemicsScreens.CourseAttendanceScreen.name) {
                            popUpTo(0) { inclusive = true }
                        }
                        return@LaunchedEffect
                    }
                }

                // If email not found in students or professors → force login
                navController.navigate(TrackdemicsScreens.RoleScreen.name) {
                    popUpTo(0) { inclusive = true }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // In case of any error, go to Role/Login screen
                navController.navigate(TrackdemicsScreens.RoleScreen.name) {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else {
            // No user is signed in → go to Role/Login screen
            navController.navigate(TrackdemicsScreens.RoleScreen.name) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
