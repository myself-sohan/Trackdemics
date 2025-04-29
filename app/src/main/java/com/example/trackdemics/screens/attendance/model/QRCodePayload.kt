package com.example.trackdemics.screens.attendance.model

data class QrCodePayload(
    val sessionId: String,
    val courseCode: String,
    val action: String, // "entry" or "exit"
    val timestamp: Long
)
