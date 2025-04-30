package com.example.trackdemics.screens.attendance.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.screens.home.components.QrCodeDialog
import com.example.trackdemics.ui.theme.onSurfaceLight
import com.example.trackdemics.utils.incrementClassesTaken
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfessorAttendanceCard(
    navController: NavController,
    course: ProfessorCourse,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    var showQrDialog = remember { mutableStateOf(false) }
    var showDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                showDialog.value = true
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White)
    )
    {
        if (showDialog.value) {
            ActionDialog(
                onDismissRequest = { showDialog.value = false },
                onEditAttendance = {
                    showDialog.value = false
                },
                onDownloadPdf = {
                    showDialog.value = false
                },
                onGenerateQr = {
                    coroutineScope.launch {
                        val qrContent = "${course.courseCode}_${System.currentTimeMillis()}"
                        val generatedAt = System.currentTimeMillis()
                        val expiresAt = generatedAt + (1 * 60 * 60 * 1000) // 1 hour later

                        // Save QR session to Firestore
                        val qrData = mapOf(
                            "course_code" to course.courseCode,
                            "generated_at" to generatedAt,
                            "expires_at" to expiresAt,
                            "qr_content" to qrContent
                        )

                        FirebaseFirestore.getInstance()
                            .collection("qr_codes")
                            .add(qrData)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(
                                    context,
                                    "QR Code generated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        // After saving QR code
                        incrementClassesTaken(course.courseCode)
                        showDialog.value = false

                    }
                },
                onDeleteCourse = {
                    showDialog.value = false
                }
            )
        }
        if (showQrDialog.value) {
            QrCodeDialog(
                "TEST",
                onDismiss = { showQrDialog.value = false },
                onScanSuccess = {
                    showQrDialog.value = false
                    Toast.makeText(context, "QR Scanned Successfully", Toast.LENGTH_SHORT).show()
                }
            )
        }
        Box(
            modifier = Modifier.Companion
                .background(
                    Brush.Companion.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.colorScheme.outlineVariant,
                            //MaterialTheme.colorScheme.outline,
                        )
                    )
                )
        )
        {
            Column(modifier = Modifier.Companion.padding(16.dp)) {
                Text(
                    text = course.courseName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color.Companion.White
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

                Text(
                    text = course.courseCode,
                    fontSize = 19.sp,
                    color = Color.Companion.White
                )

                Spacer(modifier = Modifier.Companion.height(12.dp))

                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Attended",
                        fontSize = 14.sp,
                        color = onSurfaceLight.copy(alpha = 0.6f)
                    )

                    Text(
                        text = "${course.branch}/${course.semester}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = Color.Companion.White
                    )
                }
            }
        }
    }
}

@Composable
fun ActionDialog(
    onDismissRequest: () -> Unit,
    onEditAttendance: () -> Unit,
    onDownloadPdf: () -> Unit,
    onGenerateQr: () -> Unit,
    onDeleteCourse: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Select Action", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DialogOption(text = "Edit Attendance", onClick = onEditAttendance)
                DialogOption(text = "Download PDF", onClick = onDownloadPdf)
                DialogOption(text = "Generate QR", onClick = onGenerateQr)
                DialogOption(text = "Delete Course", onClick = onDeleteCourse)
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
fun DialogOption(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    )
}