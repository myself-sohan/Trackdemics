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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.trackdemics.screens.attendance.components.AddCourseCard
import com.example.trackdemics.screens.attendance.components.AddCourseForm
import com.example.trackdemics.screens.attendance.components.ProfessorAttendanceCard
import com.example.trackdemics.screens.attendance.components.StudentAttendanceCard
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.screens.attendance.model.StudentCourse
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun StudentAttendanceScreen(
    navController: NavController
)
{
    val openDialog = remember { mutableStateOf(false) }
    // ✅ Maintain dynamic list of courses
    val courses = remember { mutableStateListOf<StudentCourse>() }
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
    )
    {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
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
            )
            {
                AddCourseCard {
                    openDialog.value = true
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if(openDialog.value)
                AddCourseForm(
                    openDialog = openDialog,
                    isStudent = true
                )
                {course, semester, branch ->
                    courses.add(
                        StudentCourse(
                            name = course.name,
                            code = course.code,
                            sem = semester,
                            total = 6,
                            attended = 7

                        )
                    )
                }
            if (courses.isNotEmpty())
            {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(courses.size) { index ->
                        StudentAttendanceCard(
                            course = courses[index],
                            navController = navController
                        )
                    }
                }
            }
            else {
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



