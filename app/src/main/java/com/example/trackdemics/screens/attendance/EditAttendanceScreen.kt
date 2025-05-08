package com.example.trackdemics.screens.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun EditAttendanceScreen(
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
        data class Student(
            val rollNumber: String,
            val name: String
        )
        data class AttendanceEntry(
            val student: Student,
            val isPresent: Boolean
        )
        data class AttendanceRecord(
            val date: String, // Format: "dd/MM/yyyy"
            val timestamp: String, // Format: "HH:mm a" e.g., "09:00 AM"
            val attendance: List<AttendanceEntry>
        )

        val students = List(40) { index ->
            val roll = "B22CS%03d".format(index + 1)
            val name = "Student ${index + 1}"
            Student(roll, name)
        }

// Sample attendance on three random dates, one date with two timestamps
        val dummyAttendanceRecords = listOf(
            AttendanceRecord(
                date = "01/04/2025",
                timestamp = "09:00 AM",
                attendance = students.map { student ->
                    AttendanceEntry(student, isPresent = listOf(true, false).random())
                }
            ),
            AttendanceRecord(
                date = "03/04/2025",
                timestamp = "09:00 AM",
                attendance = students.map { student ->
                    AttendanceEntry(student, isPresent = listOf(true, false).random())
                }
            ),
            AttendanceRecord(
                date = "03/04/2025",
                timestamp = "01:00 PM",
                attendance = students.map { student ->
                    AttendanceEntry(student, isPresent = listOf(true, false).random())
                }
            )
        )

        Column(
            modifier = Modifier
                .padding(it)
        )
        {

        }
    }
}
