package com.example.trackdemics.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.screens.attendance.components.StudentAttendanceCard
import com.example.trackdemics.screens.attendance.components.SemesterSelector
import com.example.trackdemics.screens.attendance.model.StudentCourseAttendance
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun StudentAttendanceScreen(navController: NavController) {
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    val courses = sampleCourses()
    var selectedSemester = remember { mutableStateOf("Semester 1") }

    // 🔥 Fetch saved semester from Firestore initially
    LaunchedEffect(Unit) {
        val email = auth.currentUser?.email?.trim()?.lowercase()
        if (email != null) {
            try {
                val snapshot = firestore.collection("students")
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                val doc = snapshot.documents.firstOrNull()
                val savedSemester = doc?.getString("semester")
                if (!savedSemester.isNullOrBlank()) {
                    selectedSemester.value = savedSemester
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 🔥 Save to Firestore whenever selectedSemester changes
    LaunchedEffect(selectedSemester.value) {
        val email = auth.currentUser?.email?.trim()?.lowercase()
        if (email != null) {
            try {
                val snapshot = firestore.collection("students")
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                val doc = snapshot.documents.firstOrNull()
                doc?.reference?.update("semester", selectedSemester.value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                navController = navController,
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.inversePrimary,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.inversePrimary,
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp, vertical = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val allSemesters: List<String> = (1..8).map { "Semester $it" }

                SemesterSelector(
                    semesterOptions = allSemesters,
                    selectedSemester = selectedSemester.value,
                    onSemesterSelected = { newSemester ->
                        selectedSemester.value = newSemester
                    }
                )

                Spacer(modifier = Modifier.height(54.dp))

                if (selectedSemester.value == "Semester 6") {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(courses.size) { index ->
                            StudentAttendanceCard(course = courses[index])
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight(0.1f)
                            .fillMaxWidth(0.5f),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No Courses Found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}


fun sampleCourses(): List<StudentCourseAttendance> = listOf(
    StudentCourseAttendance("Software Engineering", "CS302", 28, 30, "Semester 6"),
    StudentCourseAttendance("Compiler Design", "CS304", 25, 28, "Semester 6"),
    StudentCourseAttendance("Cryptography and Network Security", "CS322", 18, 20, "Semester 6"),
    StudentCourseAttendance("Computer Graphics", "CS312", 20, 20, "Semester 6"),
    StudentCourseAttendance("Industry 4.0 and 6-Sigma Engineering", "ME372", 20, 20, "Semester 6"),
    StudentCourseAttendance("Indian Culture and Civilization", "HS394", 20, 20, "Semester 6")
)

