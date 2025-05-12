package com.example.trackdemics.screens.attendance.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await

@Composable
fun AdminAttendanceCard(
    navController: NavController,
    course: Map<String, Any>,
    coroutineScope: CoroutineScope,
) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var showDialog = remember { mutableStateOf(false) }
    var professorName by remember { mutableStateOf("Unknown") }

    LaunchedEffect(course["code"]) {
        val courseCode = course["code"]?.toString()?.replace(" ", "") ?: return@LaunchedEffect
        try {
            val snapshot = firestore.collection("professors")
                .whereArrayContains("handled_courses", courseCode)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            if (doc != null) {
                val firstName = doc.getString("first_name") ?: ""
                val lastName = doc.getString("last_name") ?: ""
                professorName = "$firstName $lastName".trim()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { showDialog.value = true },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        if (showDialog.value) {
            AdminActionDialog(
                onDismissRequest = { showDialog.value = false },
                onEditAttendance = {
                    showDialog.value = false
                    navController.navigate("EditAttendanceScreen")
                },
                onDownloadPdf = {
                    showDialog.value = false
                },
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = course["name"].toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = course["code"].toString(),
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
                    text = "Professor: Dr. $professorName",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminActionDialog(
    onDismissRequest: () -> Unit,
    onEditAttendance: () -> Unit,
    onDownloadPdf: () -> Unit,
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


