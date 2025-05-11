package com.example.trackdemics.screens.attendance.model

data class FirestoreAttendanceEntry(
    val uid: String,
    val rollNumber: String,
    val fullName: String,
    val email: String,
    var isPresent: Boolean
)

