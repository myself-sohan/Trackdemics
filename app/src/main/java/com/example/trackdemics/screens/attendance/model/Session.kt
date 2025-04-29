package com.example.trackdemics.screens.attendance.model

data class Session(
    val sessionId: String = "", // auto-generated
    val courseCode: String = "", // "CS302"
    val professorId: String = "", // UID of professor
    val startTime: Long = 0L, // timestamp in millis
    val endTime: Long? = null, // optional
    val studentsPresent: List<StudentAttendance> = emptyList() // scanned students
)
