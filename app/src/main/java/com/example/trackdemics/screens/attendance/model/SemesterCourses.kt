package com.example.trackdemics.screens.attendance.model

// Data class representing all courses for a semester and branch
data class SemesterCourses(
    val semester: Int,
    val branch: String, // Example: "CSE"
    val courses: List<Course>
)