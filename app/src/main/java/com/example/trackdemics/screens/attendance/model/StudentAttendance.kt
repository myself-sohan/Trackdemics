package com.example.trackdemics.screens.attendance.model

data class StudentAttendance(
    val studentId: String = "", // UID
    val entryTime: Long = 0L, // timestamp when entered
    val exitTime: Long? = null // timestamp when exited (optional if session ended early)
)
