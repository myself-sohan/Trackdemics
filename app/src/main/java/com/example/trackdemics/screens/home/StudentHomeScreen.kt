package com.example.trackdemics.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.home.components.FeatureCard
import com.example.trackdemics.screens.home.components.ProfileSection
import com.example.trackdemics.screens.home.components.SideNavigationPanel
import com.example.trackdemics.screens.home.model.FeatureItem
import com.example.trackdemics.utils.generateQrCodeBitmap
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun StudentHomeScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf<String?>(null) }
    var enrolledCourses by remember { mutableStateOf<List<String>>(emptyList()) }
    var qrContent by remember { mutableStateOf<String?>(null) }
    var showQrDialog by remember { mutableStateOf(false) }
    val user = auth.currentUser

    // Fetch user data once
    LaunchedEffect(Unit) {
        user?.email?.let { email ->
            val normalizedEmail = email.trim().lowercase()

            val studentSnapshot = firestore.collection("students")
                .whereEqualTo("email", normalizedEmail)
                .get()
                .await()

            val studentDoc = studentSnapshot.documents.firstOrNull()
            firstName = studentDoc?.getString("first_name") ?: "Student"
            enrolledCourses = studentDoc?.get("enrolled_courses") as? List<String> ?: emptyList()
        }
    }

    // Live listen to QR codes
    LaunchedEffect(enrolledCourses) {
        if (enrolledCourses.isNotEmpty()) {
            val now = System.currentTimeMillis()

            firestore.collection("qr_codes")
                .whereGreaterThan("expires_at", now)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        error.printStackTrace()
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val activeQr = snapshot.documents.firstOrNull { doc ->
                            val courseCode = doc.getString("course_code")
                            courseCode in enrolledCourses
                        }

                        if (activeQr != null) {
                            qrContent = activeQr.getString("qr_content")
                            showQrDialog = true
                        } else {
                            qrContent = null
                            showQrDialog = false
                        }
                    }
                }
        }
    }

    // QR Dialog
    if (showQrDialog && qrContent != null) {
        AlertDialog(
            onDismissRequest = { showQrDialog = false },
            title = { Text("Scan to Mark Attendance") },
            text = {
                val qrBitmap = generateQrCodeBitmap(qrContent!!)
                Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = "QR Code")
            },
            confirmButton = {
                TextButton(onClick = { showQrDialog = false }) {
                    Text("Close")
                }
            },
            containerColor = Color.White
        )
    }

    // Main UI
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                SideNavigationPanel(navController = navController)
            }
        },
        scrimColor = MaterialTheme.colorScheme.background
    ) {
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
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                ProfileSection(
                    modifier = Modifier,
                    label = "Welcome, ${firstName ?: "Student"} 👋"
                )
                StudentFeatureGrid(navController = navController)
            }
        }
    }
}


@Composable
fun StudentFeatureGrid(
    navController: NavController
) {
    val features = listOf(
        FeatureItem(
            "Attendance",
            Icons.Default.Groups,
            { navController.navigate(TrackdemicsScreens.StudentAttendanceScreen.name) }),
        FeatureItem("Results", Icons.Default.Grade) { },
        FeatureItem("College Routine", Icons.Default.Schedule) { },
        FeatureItem("Assignment", Icons.AutoMirrored.Filled.Assignment) { },
        FeatureItem("Events", Icons.Default.Event) { },
        FeatureItem("Courses", Icons.AutoMirrored.Filled.MenuBook) { }
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




