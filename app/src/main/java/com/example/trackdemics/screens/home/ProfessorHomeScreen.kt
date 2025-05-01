package com.example.trackdemics.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.home.components.FeatureCard
import com.example.trackdemics.screens.home.components.ProfileSection
import com.example.trackdemics.screens.home.components.SideNavigationPanel
import com.example.trackdemics.screens.home.model.FeatureItem
import com.example.trackdemics.screens.signup.SignUpViewModel
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfessorHomeScreen(
    signupViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val user = auth.currentUser
    var firstName by remember { mutableStateOf<String?>(null) }
    var department by remember { mutableStateOf<String?>(null) }
    var designation by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        user?.email?.let { email ->
            val normalizedEmail = email.trim().lowercase()

            val professorSnapshot = firestore.collection("professors")
                .whereEqualTo("email", normalizedEmail)
                .get()
                .await()

            val professorDoc = professorSnapshot.documents.firstOrNull()
            firstName = professorDoc?.getString("first_name") ?: "Professor"
            department = professorDoc?.getString("department") ?: "Department"
            designation = professorDoc?.getString("designation") ?: "Designation"
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(color = MaterialTheme.colorScheme.background)
            )
            {
                SideNavigationPanel(signupViewModel, navController)
            }
        },
        scrimColor = MaterialTheme.colorScheme.background
    )
    {
        Scaffold(
            topBar = {
                TrackdemicsAppBar(
                    navController = navController,
                    drawerState = drawerState,
                    isEntryScreen = false,
                    titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleTextColor = MaterialTheme.colorScheme.background
                )
            }
        )
        { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                ProfileSection(
                    modifier = Modifier,
                    "Welcome, Dr. ${firstName ?: "Professor"} 👋"
                )

                ProfessorFeatureGrid(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun ProfessorFeatureGrid(
    navController: NavController
) {
    val features = listOf(
        FeatureItem(
            "Attendance", Icons.Default.Groups) { navController.navigate("ProfessorAttendanceScreen") },
        FeatureItem("Projects", Icons.Default.Lightbulb) { },
        FeatureItem("College Routine", Icons.Default.Schedule) { navController.navigate(TrackdemicsScreens.RoutineScreen.name) },
        FeatureItem("Reminders", Icons.Default.Notifications) { /* Navigate to Reminders */ },
        FeatureItem(
            "Assignment",
            Icons.AutoMirrored.Filled.Assignment
        ) { /* Navigate to Events */ },
        FeatureItem("Courses", Icons.AutoMirrored.Filled.MenuBook) { /* Navigate to Settings */ }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(features.size) { index ->
            FeatureCard(features[index])
        }
    }
}