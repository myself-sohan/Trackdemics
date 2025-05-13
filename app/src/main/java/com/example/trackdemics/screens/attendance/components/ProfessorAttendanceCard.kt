package com.example.trackdemics.screens.attendance.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.repository.AppFirestoreService
import com.example.trackdemics.repository.generateExcelFromAttendance
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfessorAttendanceCard(
    navController: NavController,
    course: ProfessorCourse,
    coroutineScope: CoroutineScope,
    onCourseDeleted: (String) -> Unit,
    studentsInCourse: String = "30"
) {
    val context = LocalContext.current
    var showDialog = remember { mutableStateOf(false) }
    var showDeleteDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                showDialog.value = true
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    )
    {
        if (showDialog.value) {
            ProfessorActionDialog(
                onDismissRequest = { showDialog.value = false },
                onEditAttendance = {
                    showDialog.value = false
                    navController.navigate("EditAttendanceScreen/${course.courseCode}/${course.courseName}")
                },
                onDownloadPdf = {
                    coroutineScope.launch {
                        val file = generateExcelFromAttendance(
                            context = context,
                            courseCode = course.courseCode,
                            courseName = course.courseName,
                            firestore = FirebaseFirestore.getInstance()
                        )

                        if (file != null) {
                            Toast.makeText(context, "Excel saved to Downloads", Toast.LENGTH_SHORT)
                                .show()

                            // Optional: Open it
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider", // <- make sure to define this in AndroidManifest.xml
                                file
                            )
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(
                                uri,
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            )
                            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onTakeAttendance = {
                    val codeEncoded = course.courseCode.replace(" ", "%20")
                    val nameEncoded = course.courseName.replace(" ", "%20")
                    navController.navigate("CourseAttendanceScreen/$codeEncoded/$nameEncoded")
                },
                onDeleteCourse = {
                    showDialog.value = false
                    showDeleteDialog.value = true
                }
            )
        }
        if (showDeleteDialog.value) {
            ConfirmationDialog(
                courseCode = course.courseCode,
                onDismissRequest = { showDeleteDialog.value = false },
                onConfirm = {
                    coroutineScope.launch {
                        val success =
                            AppFirestoreService.removeCourseFromProfessor(course.courseCode)
                        if (success) {
                            onCourseDeleted(course.courseCode)
                            showDeleteDialog.value = false
                        }
                        Toast.makeText(
                            context,
                            "Course ${course.courseCode} deleted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
        Box(
            modifier = Modifier.Companion
        )
        {
            Column(modifier = Modifier.Companion.padding(16.dp)) {
                Text(
                    text = course.courseName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = MaterialTheme.colorScheme.surface
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

                Text(
                    text = course.courseCode,
                    fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    color = MaterialTheme.colorScheme.surface
                )

                Spacer(modifier = Modifier.Companion.height(12.dp))

                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Students Enrolled: $studentsInCourse",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 1.6f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                    )

                    Text(
                        text = "Sem ${course.semester}",
                        fontSize = 19.sp,
                        fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessorActionDialog(
    onDismissRequest: () -> Unit,
    onEditAttendance: () -> Unit,
    onDownloadPdf: () -> Unit,
    onTakeAttendance: () -> Unit,
    onDeleteCourse: () -> Unit
) {
    val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }

    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        )
        {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
            {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Title
                    Text(
                        text = "Select Action",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DialogOption(
                            icon = Icons.Default.HowToReg,
                            text = "Take Attendance",
                            onClick = {
                                onTakeAttendance()
                                onDismissRequest()
                            }
                        )
                        DialogOption(
                            icon = Icons.Default.Edit,
                            text = "Edit Attendance",
                            onClick = {
                                onEditAttendance()
                                onDismissRequest()
                            }
                        )
                        DialogOption(
                            icon = Icons.Default.Download,
                            text = "Download PDF",
                            onClick = {
                                onDownloadPdf()
                                onDismissRequest()
                            }
                        )
                        DialogOption(
                            icon = Icons.Default.Delete,
                            text = "Delete Course",
                            iconTint = Color.Red,
                            textColor = Color.Red,
                            onClick = {
                                onDeleteCourse()
                                onDismissRequest()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}


