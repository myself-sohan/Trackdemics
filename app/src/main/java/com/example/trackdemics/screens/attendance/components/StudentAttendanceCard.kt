package com.example.trackdemics.screens.attendance.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import com.example.trackdemics.repository.AppFirestoreService
import com.example.trackdemics.screens.attendance.model.StudentCourse
import com.example.trackdemics.ui.theme.onSurfaceLight
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun StudentAttendanceCard(
    course: StudentCourse,
    coroutineScope: CoroutineScope,
    onCourseDeleted: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val attended = remember { mutableIntStateOf(0) }
    val total = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {

        val studentId = auth.currentUser?.uid ?: return@LaunchedEffect
        val normalizedCourseCode = course.code.replace(" ", "")


        // 1. Fetch all attendance records for this course
        val snapshots = firestore.collection("attendance_record")
            .whereEqualTo("course_code", normalizedCourseCode)
            .get()
            .await()

        // 2. Count records where the student is marked present
        val attendedCount = snapshots.documents.count { doc ->
            val students = doc["students"] as? List<Map<String, Any>> ?: return@count false
            students.any { entry ->
                val uid = entry["uid"] as? String
                val isPresent = entry["present"] as? Boolean ?: false
                uid == studentId && isPresent
            }

        }

        attended.intValue = attendedCount

        // 3. Total classes taken
        val courseSnapshot = firestore.collection("courses")
            .document(course.code)
            .get()
            .await()

        total.intValue = courseSnapshot.getLong("classes_taken")?.toInt() ?: 0

        val matchingDocs = snapshots.documents.filter { doc ->
            val students = doc["students"] as? List<Map<String, Any>> ?: return@filter false
            students.any { entry ->
                val uid = entry["uid"] as? String
                val isPresent = entry["present"] as? Boolean ?: false
                uid == studentId && isPresent
            }
        }
        println("Found ${matchingDocs.size} sessions attended.")

    }




    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (showDeleteDialog.value) {
                ConfirmationDialog(
                    title = "Delete Course",
                    courseCode = course.code,
                    onDismissRequest = { showDeleteDialog.value = false },
                    onConfirm = {
                        coroutineScope.launch {
                            val success = AppFirestoreService.removeCourseFromStudent(course.code)
                            if (success) {
                                onCourseDeleted(course.code)
                                showDeleteDialog.value = false
                                Toast.makeText(
                                    context,
                                    "Course ${course.code} deleted successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = course.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                )
                IconButton(onClick = { showDeleteDialog.value = true })
                {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFEF5350).copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = course.code,
                fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Professor Name",
                    fontSize = 14.sp,
                    color = onSurfaceLight.copy(alpha = 0.6f)
                )
                Text(
                    text = "${attended.intValue}/${total.intValue}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}