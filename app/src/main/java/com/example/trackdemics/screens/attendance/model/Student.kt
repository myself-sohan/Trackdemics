package com.example.trackdemics.screens.attendance.model

data class Student(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val semester: String = "",   // "Semester 6" etc.
    val enrolledCourses: List<String> = emptyList(),  // list of course codes like ["CS302", "CS304"]
    val profilePicUrl: String? = null
)
