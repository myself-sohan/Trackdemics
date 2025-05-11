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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.repository.AppFirestoreService
import com.example.trackdemics.screens.attendance.components.AddCourseCard
import com.example.trackdemics.screens.attendance.components.AddCourseForm
import com.example.trackdemics.screens.attendance.components.ProfessorAttendanceCard
import com.example.trackdemics.screens.attendance.model.Course
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.screens.attendance.model.SemesterCourses
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AdminAttendanceScreen(
    navController: NavController
) {
    val firestoreService = remember { AppFirestoreService }
    val auth = remember { FirebaseAuth.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    val openDialog = remember { mutableStateOf(false) }
    val professorCourses = remember { mutableStateListOf<ProfessorCourse>() }
    val loading = remember { mutableStateOf(true) }

    val professorUid = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                auth.currentUser?.email?.let { email ->
                    val professorSnapshot = FirebaseFirestore.getInstance()
                        .collection("professors")
                        .whereEqualTo("email", email)
                        .get()
                        .await()

                    val doc = professorSnapshot.documents.firstOrNull()
                    val uid = doc?.id
                    professorUid.value = uid

                    if (uid != null) {
                        val courses = firestoreService.getProfessorCourses(uid)
                        professorCourses.clear()
                        professorCourses.addAll(courses)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loading.value = false
            }
        }
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
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
                AddCourseCard {
                    openDialog.value = true
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (openDialog.value) {
                AddCourseForm(openDialog = openDialog) { course, semester ->
                    coroutineScope.launch {
                        try {
                            professorUid.value?.let { uid ->
                                firestoreService.addCourseToProfessor(
                                    professorUid = uid,
                                    course = ProfessorCourse(
                                        courseName = course.name,
                                        courseCode = course.code,
                                        semester = semester,
                                    )
                                )
                                val updatedCourses = firestoreService.getProfessorCourses(uid)
                                professorCourses.clear()
                                professorCourses.addAll(updatedCourses)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when {
                loading.value -> {
                    CircularProgressIndicator()
                }

                professorCourses.isNotEmpty() -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(professorCourses.size) { index ->
                            ProfessorAttendanceCard(
                                course = professorCourses[index],
                                coroutineScope = coroutineScope,
                                navController = navController,
                                onCourseDeleted = { deletedCode ->
                                    professorCourses.removeAll { it.courseCode == deletedCode }
                                }
                            )
                        }
                    }
                }

                else -> {
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


