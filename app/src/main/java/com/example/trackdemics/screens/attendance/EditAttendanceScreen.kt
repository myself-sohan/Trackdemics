package com.example.trackdemics.screens.attendance

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.screens.attendance.components.ConfirmationDialog
import com.example.trackdemics.screens.attendance.components.DatePickerField
import com.example.trackdemics.screens.attendance.model.FirestoreAttendanceEntry
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditAttendanceScreen(
    navController: NavController
) {
    var selectedDate by remember { mutableStateOf("01/04/2025") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    val attendanceRecords = remember {
        mutableStateOf<Map<String, List<Pair<String, List<FirestoreAttendanceEntry>>>>>(emptyMap())
    }
    val availableDates = remember { mutableStateOf<List<String>>(emptyList()) }
    val timestampsForDate = remember { mutableStateOf<List<String>>(emptyList()) }
    val selectedTimestamp = remember { mutableStateOf<String?>(null) }
    val attendanceState =
        remember { mutableStateOf<SnapshotStateList<FirestoreAttendanceEntry>>(mutableStateListOf()) }

    LaunchedEffect(Unit) {
        firestore.collection("attendance_record")
            .get()
            .addOnSuccessListener { snapshot ->
                val grouped = snapshot.documents.groupBy { it.getString("session_date") ?: "" }
                    .mapValues { (_, docs) ->
                        docs.mapNotNull { doc ->
                            val timestamp =
                                doc.getLong("timestamp")?.toString() ?: return@mapNotNull null
                            val students = (doc["students"] as? List<Map<String, Any>>)?.map {
                                FirestoreAttendanceEntry(
                                    uid = it["uid"] as String,
                                    rollNumber = it["rollNumber"] as String,
                                    fullName = it["fullName"] as String,
                                    email = it["email"] as String,
                                    isPresent = it["present"] as Boolean
                                )
                            } ?: emptyList()
                            timestamp to students
                        }
                    }

                attendanceRecords.value = grouped
                availableDates.value = grouped.keys.sorted()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load attendance", Toast.LENGTH_SHORT).show()
            }
    }


    val timestamps = attendanceRecords.value[selectedDate]?.map { it.first } ?: emptyList()
    val originalState = remember(attendanceState) { attendanceState.value.map { it.copy() } }
    val showEditDialog = remember { mutableStateOf(false) }
    val showRestoreDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                navController = navController,
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    )
    { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(0.7f),
                            MaterialTheme.colorScheme.primary.copy(0.9f),
                            MaterialTheme.colorScheme.primary.copy(0.7f),
                        )
                    )
                )
        )
        {
            if (showRestoreDialog.value) {
                if (showRestoreDialog.value) {
                    ConfirmationDialog(
                        courseCode = null,
                        onDismissRequest = { showRestoreDialog.value = false },
                        onConfirm = {
                            showRestoreDialog.value = false

                            // Reset the selected students to original data
                            attendanceState.value = originalState.toMutableStateList()

                            // Optional: Show a Toast
                            Toast.makeText(
                                context,
                                "Attendance restored to Original.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        rightButtonLabel = "Reset",
                        leftButtonLabel = "Cancel",
                        title = "Restore Attendance?",
                        message1 = "Restore Attendance back to Original?",
                        message2 = " All Changes will be lost!",
                        titleIcon = Icons.Default.Warning,
                        rightButtonIcon = Icons.Default.Restore,
                        rightButtonColor = MaterialTheme.colorScheme.error
                    )
                }

            }
            if (showEditDialog.value) {
                ConfirmationDialog(
                    courseCode = null,
                    onDismissRequest = { showEditDialog.value = false },
                    onConfirm = {
                        showEditDialog.value = false
                        // ⬇️ Your submit logic here:
                        //println("Submitting data: ${selectedCards.value}")
                        Toast.makeText(
                            context,
                            "Attendance Report Editted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        // You can pass it to a ViewModel function, Firestore, etc.
                    },
                    rightButtonLabel = "Edit",
                    leftButtonLabel = "Cancel",
                    title = "Save Changes?",
                    message1 = "Are you sure you want to edit attendance? ",
                    message2 = "Once submitted, previous data will be lost..",
                    titleIcon = Icons.AutoMirrored.Filled.Send,
                    rightButtonIcon = Icons.Default.Edit,
                    rightButtonColor = MaterialTheme.colorScheme.secondary
                )
            }
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
            {

                // Title (center aligned with gradient text)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = "📋 Attendance Report List",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            brush = Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.onBackground
                                )
                            ),
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Row: Course Code on left, Date Picker on right (balanced)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    // Surface Card for Course Code
                    Spacer(
                        modifier = Modifier
                            .width(4.dp)
                            .weight(0.25f)
                    )
                    Card(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .weight(2.5f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 22.dp,
                            pressedElevation = 13.dp
                        )
                    )
                    {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📘 CSE 301",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .width(4.dp)
                            .weight(0.25f)
                    )

                    // Date Picker Field (styled and aligned)
                    Box(
                        modifier = Modifier
                            .weight(3f),
                        contentAlignment = Alignment.CenterEnd
                    )
                    {
                        DatePickerField(
                            selectedDate = selectedDate,
                            onDateSelected = { date ->
                                selectedDate = date
                                timestampsForDate.value =
                                    attendanceRecords.value[date]?.map { it.first } ?: emptyList()
                                selectedTimestamp.value = timestampsForDate.value.firstOrNull()

                                selectedTimestamp.value?.let { ts ->
                                    val entryList =
                                        attendanceRecords.value[date]?.firstOrNull { it.first == ts }?.second
                                    attendanceState.value =
                                        entryList?.toMutableStateList() ?: mutableStateListOf()
                                }
                            },
                        )
                    }

//                    Spacer(
//                        modifier = Modifier
//                            .width(4.dp)
//                            .weight(0.25f)
//                    )
                }
                if (timestamps.size > 1) {
                    selectedTimestamp.value = timestamps.firstOrNull()
                    Spacer(modifier = Modifier.height(1.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.4f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            OutlinedTextField(
                                value = selectedTimestamp.value.orEmpty(),
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(
                                        text = "Choose Time",
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Pick time",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                },
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    //fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                                    color = MaterialTheme.colorScheme.onPrimary
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier,
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true,
                            )
                            DropdownMenu(
                                expanded = expanded,
                                offset = DpOffset(18.dp, 3.dp),
                                onDismissRequest = { expanded = false }
                            )
                            {
                                timestamps.forEach { time ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = time,
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontFamily = FontFamily(Font(R.font.lobster_regular)),
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        },
                                        onClick = {
                                            selectedTimestamp.value = time
                                            val entryList =
                                                attendanceRecords.value[selectedDate]?.firstOrNull { it.first == time }?.second
                                            attendanceState.value = entryList?.toMutableStateList()
                                                ?: mutableStateListOf()
                                        }

                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                {
                    AttendanceReportGrid(
                        attendanceState = attendanceState.value,
                        onToggleAttendance = { entry ->
                            val index = attendanceState.value.indexOf(entry)
                            attendanceState.value[index] = entry.copy(isPresent = !entry.isPresent)
                        }
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    Spacer(
                        modifier = Modifier
                            .weight(0.25f)
                    )
                    Button(
                        onClick = {
                            // Simulate saving the updated attendanceState
                            // Replace with your update logic
                            showEditDialog.value = true
                        },
                        elevation = ButtonDefaults.buttonElevation(10.dp),
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    {
                        Text(
                            text = "Edit",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(0.25f)
                    )
                    Button(
                        onClick = {
                            showRestoreDialog.value = true
                        },
                        elevation = ButtonDefaults.buttonElevation(10.dp),
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                    {
                        Text(
                            text = "Restore",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(0.25f)
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceReportGrid(
    attendanceState: SnapshotStateList<FirestoreAttendanceEntry>,
    onToggleAttendance: (FirestoreAttendanceEntry) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(attendanceState.size) { index ->
            val entry = attendanceState[index]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onToggleAttendance(entry)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = entry.rollNumber,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "85%",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(start = 3.dp),
                        fontFamily = FontFamily(Font(R.font.lobster_regular)),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    )
                    {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    if (entry.isPresent) Color(0xFF4CAF50) else Color(0xFFF44336),
                                    shape = CircleShape
                                )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        )
                        {
                            Text(
                                text = if (entry.isPresent) "Present" else "Absent",
                                color = if (entry.isPresent) Color(0xFF4CAF50) else Color(0xFFF44336),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Tap to toggle",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
