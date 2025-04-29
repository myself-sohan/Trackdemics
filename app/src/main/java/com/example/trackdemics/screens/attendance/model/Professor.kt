package com.example.trackdemics.screens.attendance.model

data class Professor(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val handledCourses: List<String> = emptyList(),
    val designation: String = "",
    val department: String = "",
    val registered: Boolean = false
)
