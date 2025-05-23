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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.repository.AppFirestoreService.loadStudentCourses
import com.example.trackdemics.screens.attendance.components.AddCourseCard
import com.example.trackdemics.screens.attendance.components.AddCourseForm
import com.example.trackdemics.screens.attendance.components.StudentAttendanceCard
import com.example.trackdemics.screens.attendance.model.StudentCourse
import com.example.trackdemics.widgets.LottieFromAssets
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun StudentAttendanceScreen(
    navController: NavController
) {
    val openDialog = remember { mutableStateOf(false) }
    val courses = remember { mutableStateListOf<StudentCourse>() }
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            loadStudentCourses(courses)
        }
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                },
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.inversePrimary,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AddCourseCard(
                    selectedOption = null
                )
                {
                    openDialog.value = true
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (openDialog.value) {
                AddCourseForm(
                    openDialog = openDialog,
                    isStudent = true
                ) { course, semester ->
                    coroutineScope.launch {
                        // 1. Update Firestore
                        auth.currentUser?.email?.let { email ->
                            val studentSnapshot = firestore.collection("students")
                                .whereEqualTo("email", email.trim().lowercase())
                                .get()
                                .await()

                            val studentDoc = studentSnapshot.documents.firstOrNull()
                            studentDoc?.reference?.update(
                                "enrolled_courses", FieldValue.arrayUnion(course.code)
                            )?.await()

                            // 2. Reload updated courses
                            loadStudentCourses(courses)
                        }
                    }
                    openDialog.value = false
                }
            }

            if (courses.isNotEmpty()) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(courses.size) { index ->
                        StudentAttendanceCard(
                            course = courses[index],
                            coroutineScope,
                            onCourseDeleted = { deletedCode ->
                                courses.removeAll { it.code == deletedCode }
                            }
                        )
                    }
                }
            } else {
                EmptyState()
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.45f)
                .fillMaxWidth(0.85f),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieFromAssets(
                    assetName = "no_data.json",
                    modifier = Modifier
                        .fillMaxSize()
                )
//                Text(
//                    text = "No Courses Found",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.error,
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center
//                )
            }
        }
    }
}



