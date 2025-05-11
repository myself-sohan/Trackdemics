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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.trackdemics.screens.home.components.FeatureCard
import com.example.trackdemics.screens.home.components.ProfileSection
import com.example.trackdemics.screens.home.components.SideNavigationPanel
import com.example.trackdemics.screens.home.model.FeatureItem
import com.example.trackdemics.screens.signup.SignUpViewModel
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun AdminHomeScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(color = MaterialTheme.colorScheme.background)
            )
            {
                SideNavigationPanel(signUpViewModel, navController)
            }
        },
        scrimColor = MaterialTheme.colorScheme.background
    )
    {
        Scaffold(
            topBar = {
                TrackdemicsAppBar(
                    onBackClick = {
                        navController.popBackStack()
                    },
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
                    collection = "professors",
                    modifier = Modifier,
                    label = "Hello Admin"
                )
                AdminFeatureGrid(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun AdminFeatureGrid(
    navController: NavController
)
{
    val features = listOf(
        FeatureItem("Attendance", Icons.Default.Groups) { navController.navigate("AdminAttendanceScreen") },
        FeatureItem("Results", Icons.Default.Grade) { /* Navigate to Results */ },
        FeatureItem("College Routine", Icons.Default.Schedule) { /* Navigate to Routine */ },
        FeatureItem(
            "Account Control",
            Icons.Default.ManageAccounts
        ) { /* Navigate to Reminders */ },
        FeatureItem("Events", Icons.Default.EventAvailable) { /* Navigate to Events */ },
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