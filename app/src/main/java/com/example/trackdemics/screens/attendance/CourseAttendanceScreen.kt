package com.example.trackdemics.screens.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.screens.attendance.components.AttendanceTrendGraph
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.example.trackdemics.screens.attendance.components.CourseDetails
import com.example.trackdemics.screens.attendance.components.TakeAttendanceSection

@Composable
fun CourseAttendanceScreen(
    navController: NavController
)
{
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
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top
        )
        {
            CourseDetails(
                modifier = Modifier.weight(0.25f),
                code = "CS 302",
                totalClass = 44,
                totalStudents = 26
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Surface(
                modifier = Modifier.weight(0.5f),
                color = Color.Transparent
            )
            {
                val dummyAttendanceData = listOf(
                    "Thu" to 88,
                    "Fri" to 80,
                    "Mon" to 85,
                    "Tue" to 92,
                    "Wed" to 46,
                )
                AttendanceTrendGraph(
                    attendanceData = dummyAttendanceData,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            TakeAttendanceSection(
                modifier = Modifier.weight(0.3f)
            )
            {
                navController.navigate("StudentListScreen")
            }
        }
    }
}



