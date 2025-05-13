package com.example.trackdemics.screens.attendance

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.attendance.components.ConfirmationDialog
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment


@Composable
fun StudentListScreen(
    navController: NavController,
    code: String,
    name: String,
) {
    val showBackDialog = remember { mutableStateOf(false) }
    BackHandler(enabled = true) {
        showBackDialog.value = true
       }

    if (showBackDialog.value) {
        ConfirmationDialog(
            courseCode = code,
            onDismissRequest = { showBackDialog.value = false },
            onConfirm = {
                showBackDialog.value = false
                navController.popBackStack() // Or navigate to a safer screen
            },
            rightButtonLabel = "Exit",
            leftButtonLabel = "Stay",
            title = "Leave Attendance Session?",
            message1 = "Leaving now will discard the ongoing attendance session for ",
            message2 = ". Do you want to continue?",
            titleIcon = Icons.Default.Warning,
            rightButtonIcon = Icons.Default.Restore,
            rightButtonColor = MaterialTheme.colorScheme.error
        )
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    showBackDialog.value = true
                },
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    ) { paddingValues ->

        val selectedCards = remember { mutableStateOf(setOf<String>()) }
        val showSubmitDialog = remember { mutableStateOf(false) }
        val showResetDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current
        val codeEncoded = code.replace(" ", "%20")
        val nameEncoded = name.replace(" ", "%20")
        if (showSubmitDialog.value) {
            ConfirmationDialog(
                courseCode = code,
                onDismissRequest = { showSubmitDialog.value = false },
                onConfirm = {
                    showSubmitDialog.value = false
                    val firestore = FirebaseFirestore.getInstance()
                    val auth = FirebaseAuth.getInstance()
                    val sessionDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val timestamp = System.currentTimeMillis()

                    val presentRolls = selectedCards.value.map { it.lowercase() }.toSet()

                    firestore.collection("students")
                        .whereArrayContains("enrolled_courses", code)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val studentRecords = snapshot.documents.mapNotNull { doc ->
                                val email = doc.getString("email") ?: return@mapNotNull null
                                val uid = doc.id
                                val rollNumber = email.substringBefore("@").lowercase()
                                val fullName =
                                    "${doc.getString("first_name") ?: ""} ${doc.getString("last_name") ?: ""}".trim()

                                mapOf(
                                    "uid" to uid,
                                    "rollNumber" to rollNumber,
                                    "fullName" to fullName,
                                    "email" to email,
                                    "present" to (rollNumber in presentRolls)
                                )
                            }
                            val attendanceRecord = mapOf(
                                "course_code" to code.replace(" ", ""), // Normalized
                                "session_date" to sessionDate,
                                "timestamp" to timestamp,
                                "taken_by" to auth.currentUser?.uid,
                                "students" to studentRecords
                            )

                            firestore.collection("attendance_record")
                                .add(attendanceRecord)

                                .addOnSuccessListener {
                                    val courseRef = firestore.collection("courses").document(code.replace(" ", ""))
                                    firestore.runTransaction { transaction ->
                                        val snapshot = transaction.get(courseRef)
                                        val currentCount = snapshot.getLong("classes_taken") ?: 0
                                        transaction.update(
                                            courseRef,
                                            "classes_taken",
                                            currentCount + 1
                                        )
                                    }.addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Attendance saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("${TrackdemicsScreens.CourseAttendanceScreen.name}/$codeEncoded/$nameEncoded")
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Failed to update classes count",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Failed to save attendance",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Failed to fetch student data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                rightButtonLabel = "Submit",
                leftButtonLabel = "Cancel",
                title = "Submit Attendance?",
                message1 = "Are you sure you want to submit attendance for ",
                message2 = "? Once submitted, this session will end.",
                titleIcon = Icons.AutoMirrored.Filled.Send,
                rightButtonIcon = Icons.Default.TaskAlt,
                rightButtonColor = MaterialTheme.colorScheme.secondary
            )
        }
        if (showResetDialog.value) {
            if (showResetDialog.value) {
                ConfirmationDialog(
                    courseCode = null,
                    onDismissRequest = { showResetDialog.value = false },
                    onConfirm = {
                        showResetDialog.value = false

                        // Reset the selected students
                        selectedCards.value = emptySet()

                        // Optional: Show a Toast
                        Toast.makeText(
                            context,
                            "Attendance reset. Proceed Again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    rightButtonLabel = "Reset",
                    leftButtonLabel = "Cancel",
                    title = "Reset Attendance?",
                    message1 = "Reset attendance and start fresh?",
                    message2 = " Are you sure you want to start over?",
                    titleIcon = Icons.Default.Warning,
                    rightButtonIcon = Icons.Default.Restore,
                    rightButtonColor = MaterialTheme.colorScheme.error
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp), // some bottom space
            verticalArrangement = Arrangement.Top
        )
        {
            CourseDetails(
                name = name,
                code = code
            )

            // --- Date & Time Row ---
            val current = LocalDateTime.now()
            val formattedDate = current.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            val formattedTime = current.format(DateTimeFormatter.ofPattern("hh:mm a"))

            DateTimeSection(
                formattedDate = formattedDate,
                formattedTime = formattedTime
            )

            val firestore = FirebaseFirestore.getInstance()
            val enrolledStudents =
                remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) } // Pair<UID, RollNumber>

            LaunchedEffect(code) {
                firestore.collection("students")
                    .whereArrayContains("enrolled_courses", code.replace(" ", ""))
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val studentList = snapshot.documents.mapNotNull { doc ->
                            val email = doc.getString("email") ?: return@mapNotNull null
                            val uid = doc.id
                            val roll = email.substringBefore("@")
                            uid to roll
                        }
                        enrolledStudents.value = studentList
                    }
            }


            AttendanceSection(
                students = enrolledStudents.value.map { it.second },
                selectedCards = selectedCards,
                onCardClick = { roll ->
                    selectedCards.value = if (selectedCards.value.contains(roll)) {
                        selectedCards.value - roll // Deselect
                    } else {
                        selectedCards.value + roll // Select
                    }
                }
            )
            ActionButtonsSection(
                onSubmitClick = {
                    showSubmitDialog.value = true
                },
                onResetClick = {
                    showResetDialog.value = true
                }
            )


        }
    }
}

@Composable
fun ActionButtonsSection(
    onSubmitClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val isVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400) // Delay to allow fade-in effect
        isVisible.value = true
    }

    AnimatedVisibility(
        visible = isVisible.value,
        enter = fadeIn(animationSpec = tween(20)) + scaleIn(tween(1000)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel Button
            Surface(
                onClick = onResetClick,
                shape = RoundedCornerShape(20),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Reset",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Submit Button
            Surface(
                onClick = onSubmitClick,
                shape = RoundedCornerShape(20),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Submit",
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceSection(
    students: List<String>,
    selectedCards: MutableState<Set<String>>,
    onCardClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        students.chunked(4).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                rowItems.forEach { student ->
                    val isSelected = selectedCards.value.contains(student)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onCardClick(student) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected)
                                Color.Green
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = student.uppercase(),
                                color = if (isSelected)
                                    Color.Black
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                if (rowItems.size < 4) {
                    repeat(4 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}


@Composable
fun DateTimeSection(
    formattedDate: String,
    formattedTime: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clip(RoundedCornerShape(topEndPercent = 33, bottomStartPercent = 33))
                .height(70.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clip(RoundedCornerShape(topStartPercent = 33, bottomEndPercent = 33))
                .height(70.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
fun CourseDetails(
    name: String,
    code: String
) {
    // --- Gradient Header with Course Code and Name ---
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            var visible = remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(300)
                visible.value = true
            }

            AnimatedVisibility(
                visible = visible.value,
                enter = slideInHorizontally(tween(1500)) + scaleIn(tween(1000))
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.tertiary,
                                        MaterialTheme.colorScheme.tertiaryContainer
                                    ),
                                    center = Offset.Unspecified,
                                    radius = 150f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    )
                    {
                        val codeLetters = code.take(2)
                        val codeNumbers = code.drop(2)

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                ) {
                                    append("$codeLetters\n")
                                }
                                withStyle(
                                    style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                                    )
                                ) {
                                    append(codeNumbers)
                                }
                            },
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = visible.value,
                enter = slideInHorizontally(tween(1500)) { it } + scaleIn(tween(1000))
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    maxLines = 4
                )
            }
        }
    }
}
