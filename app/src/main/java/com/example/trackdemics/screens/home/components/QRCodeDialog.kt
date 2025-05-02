package com.example.trackdemics.screens.home.components

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import com.example.trackdemics.utils.decodeQrFromBitmap
import com.example.trackdemics.utils.generateQrCodeBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QrCodeDialog(
    content: String,
    onDismiss: () -> Unit,
    onScanSuccess: (String) -> Unit = {},
    onScanFailure: () -> Unit = {}
) {
    val bitmap = generateQrCodeBitmap(content)
    val infiniteTransition = rememberInfiniteTransition()
    val pulse = infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val parts = content.split("_")
    val courseCode = parts.getOrNull(0)?.replace(" ", "") ?: "Unknown"
    val rawTimestamp = parts.getOrNull(1)?.toLongOrNull()
    val formattedTimestamp = rawTimestamp?.let {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(it))
    } ?: "Invalid timestamp"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Scan for attendance 🙋‍♂️🙋‍♀️",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(pulse.value)
                        .padding(8.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        append("Course: ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                            )
                        ) { append(courseCode) }
                    },
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formattedTimestamp,
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                elevation = ButtonDefaults.buttonElevation(15.dp),
                onClick = {
                    val decoded = decodeQrFromBitmap(bitmap)
                    if (decoded != null) {
                        val parts = decoded.split("_")
                        if (parts.size == 2) {
                            val scannedCode = parts[0].replace(" ", "")
                            val auth = FirebaseAuth.getInstance()
                            val firestore = FirebaseFirestore.getInstance()
                            val user = auth.currentUser

                            user?.let { currentUser ->
                                val attendanceEntry = mapOf(
                                    "courseCode" to scannedCode,
                                    "studentId" to currentUser.uid,
                                    "timestamp" to System.currentTimeMillis(),
                                    "scannedQrContent" to decoded
                                )

                                firestore.collection("attendance_record")
                                    .add(attendanceEntry)
                                    .addOnSuccessListener { onScanSuccess(scannedCode) }
                                    .addOnFailureListener {
                                        Log.e("QrScan", "Failed to mark attendance", it)
                                        onScanFailure()
                                    }
                            } ?: onScanFailure()
                        } else onScanFailure()
                    } else onScanFailure()
                }
            ) {
                Text(text = "Scan QR", fontSize = 16.sp)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}
