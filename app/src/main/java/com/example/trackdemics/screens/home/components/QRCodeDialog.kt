package com.example.trackdemics.screens.home.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import com.example.trackdemics.utils.decodeQrFromBitmap
import com.example.trackdemics.utils.generateQrCodeBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun QrCodeDialog(
    content: String,
    onDismiss: () -> Unit,
    onScanSuccess: (String) -> Unit = {},
    onScanFailure: () -> Unit = {}
) {
    val bitmap = generateQrCodeBitmap(content)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Scan to mark attendance!") },
        text = {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "QR Code"
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val decoded = decodeQrFromBitmap(bitmap)
                    if (decoded != null) {
                        val parts = decoded.split("_")
                        if (parts.size == 2) {
                            val courseCode = parts[0].replace(" ", "")
                            val timestamp = parts[1]
                            val auth = FirebaseAuth.getInstance()
                            val firestore = FirebaseFirestore.getInstance()
                            val user = auth.currentUser

                            user?.let { currentUser ->
                                val attendanceEntry = mapOf(
                                    "courseCode" to courseCode,
                                    "studentId" to currentUser.uid,
                                    "timestamp" to System.currentTimeMillis(),
                                    "scannedQrContent" to decoded
                                )

                                firestore.collection("attendance_record")
                                    .add(attendanceEntry)
                                    .addOnSuccessListener {
                                        onScanSuccess(courseCode)
                                    }
                                    .addOnFailureListener {
                                        Log.e("QrScan", "Failed to mark attendance", it)
                                        onScanFailure()
                                    }
                            } ?: onScanFailure()
                        } else {
                            onScanFailure()
                        }
                    } else {
                        onScanFailure()
                    }
                }
            ) {
                Text("Scan QR")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
