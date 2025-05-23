package com.example.trackdemics.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.signup.SignUpViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


@Composable
fun SideNavigationPanel(
    signupViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentUserEmail = Firebase.auth.currentUser?.email
    val role = remember { mutableStateOf("") }

    // Fetch role only once
    LaunchedEffect(currentUserEmail) {
        currentUserEmail?.let { email ->
            val firestore = FirebaseFirestore.getInstance()

            val collections = listOf("admin", "students", "professors")
            for (collection in collections) {
                val querySnapshot = firestore.collection(collection)
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    role.value = collection // "admin" from "admins"
                    break
                }
            }
        }
    }
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp, top = 72.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(id = R.drawable.nitm),
            contentDescription = "College Logo",
            modifier = Modifier.Companion
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
        )

        Spacer(modifier = Modifier.Companion.height(8.dp))

        Text(
            "Trackdemics",
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )

        Spacer(modifier = Modifier.Companion.height(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.Companion.height(16.dp))

        NavItem(
            "Dashboard",
            Icons.Default.Home
        ) {
            when (role.value) {
                "admin" -> navController.navigate(TrackdemicsScreens.AdminHomeScreen.name)
                "students" -> navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                "professors" -> navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
            }
        }
        NavItem(
            "Profile",
            Icons.Default.Person
        ) { /* Navigate to Profile */ }
        NavItem(
            "Settings",
            Icons.Default.Settings
        ) { /* Navigate to Settings */ }
        NavItem(
            "Logout",
            Icons.AutoMirrored.Filled.Logout
        ) {
            signupViewModel.logout()
            navController.navigate(TrackdemicsScreens.RoleScreen.name)
        }
    }
}

@Composable
fun NavItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp
        )
    }
}