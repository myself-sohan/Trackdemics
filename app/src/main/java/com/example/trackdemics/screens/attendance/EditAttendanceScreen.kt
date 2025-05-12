package com.example.trackdemics.screens.attendance

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.screens.attendance.components.AttendanceReportGrid
import com.example.trackdemics.screens.attendance.components.ConfirmationDialog
import com.example.trackdemics.screens.attendance.components.DatePickerField
import com.example.trackdemics.screens.attendance.model.FirestoreAttendanceEntry
import com.example.trackdemics.widgets.LottieFromAssets
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditAttendanceScreen(
    navController: NavController,
    courseCode: String,
    courseName: String,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf("2025-05-10") }
    var expanded by remember { mutableStateOf(false) }
    val reloadTrigger = remember { mutableIntStateOf(0) }

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
    val selectedDocId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(courseCode, reloadTrigger.intValue) {
        firestore.collection("attendance_record")
            .whereEqualTo("course_code", courseCode.replace(" ", ""))
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(context, "Failed to load attendance", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
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

                    // Auto-select new data if available
                    if (grouped.containsKey(selectedDate)) {
                        timestampsForDate.value =
                            grouped[selectedDate]?.map { it.first } ?: emptyList()
                        selectedTimestamp.value = timestampsForDate.value.firstOrNull()
                    } else if (grouped.isNotEmpty()) {
                        selectedDate = grouped.keys.first()
                        timestampsForDate.value =
                            grouped[selectedDate]?.map { it.first } ?: emptyList()
                        selectedTimestamp.value = timestampsForDate.value.firstOrNull()
                    }

                    selectedTimestamp.value?.let { ts ->
                        val entryList =
                            grouped[selectedDate]?.firstOrNull { it.first == ts }?.second
                        attendanceState.value =
                            entryList?.toMutableStateList() ?: mutableStateListOf()
                    }
                }
            }
    }
    LaunchedEffect(
        selectedDate,
        selectedTimestamp.value,
        attendanceRecords.value,
        reloadTrigger.intValue
    ) {
        val docs = attendanceRecords.value[selectedDate]
        val timestamp = selectedTimestamp.value

        val allDocuments = firestore.collection("attendance_record")
            .whereEqualTo("course_code", courseCode.replace(" ", ""))
            .whereEqualTo("session_date", selectedDate)
            .get()
            .addOnSuccessListener { snapshot ->
                val matchingDoc = snapshot.documents.firstOrNull {
                    it.getLong("timestamp")?.toString() == timestamp
                }
                selectedDocId.value = matchingDoc?.id
                Log.d("Attendance", "Selected Doc ID: ${selectedDocId.value}")
            }
            .addOnFailureListener {
                selectedDocId.value = null
                Log.e("Attendance", "Failed to find matching document for edit", it)
            }
    }

    LaunchedEffect(selectedDate, selectedTimestamp.value, reloadTrigger.intValue) {
        selectedTimestamp.value?.let { ts ->
            val entryList = attendanceRecords.value[selectedDate]
                ?.firstOrNull { it.first == ts }
                ?.second

            if (entryList != null) {
                val updatedList = viewModel.calculateAttendancePercentagesForStudents(
                    firestore = firestore,
                    courseCode = courseCode,
                    students = entryList
                )

                attendanceState.value = updatedList.toMutableStateList()
            } else {
                attendanceState.value = mutableStateListOf()
            }
        }
    }


    val recordsForSelectedDate = attendanceRecords.value[selectedDate]
    val hasAttendance = !recordsForSelectedDate.isNullOrEmpty()
    val timestamps = recordsForSelectedDate?.map { it.first } ?: emptyList()

    val showEditDialog = remember { mutableStateOf(false) }
    val showRestoreDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = { navController.popBackStack() },
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    ) { padding ->
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
        ) {
            // Confirmation Dialogs
            if (showRestoreDialog.value) {
                ConfirmationDialog(
                    courseCode = null,
                    onDismissRequest = { showRestoreDialog.value = false },
                    onConfirm = {
                        showRestoreDialog.value = false
                        reloadTrigger.intValue++
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
                    message2 = "All changes will be lost!",
                    titleIcon = Icons.Default.Warning,
                    rightButtonIcon = Icons.Default.Restore,
                    rightButtonColor = MaterialTheme.colorScheme.error
                )
            }

            if (showEditDialog.value) {
                ConfirmationDialog(
                    courseCode = null,
                    onDismissRequest = { showEditDialog.value = false },
                    onConfirm = {
                        showEditDialog.value = false
                        selectedDocId.value?.let { docId ->
                            viewModel.updateAttendanceInFirestore(
                                documentId = docId,
                                updatedEntries = attendanceState.value,
                                onComplete = { success ->
                                    Toast.makeText(
                                        context,
                                        if (success) "Attendance Report Edited Successfully" else "Update failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    reloadTrigger.intValue++
                                    navController.popBackStack()
                                }
                            )
                        } ?: Toast.makeText(
                            context,
                            "Error: No session selected",
                            Toast.LENGTH_SHORT
                        ).show()

                    },
                    rightButtonLabel = "Save",
                    leftButtonLabel = "Cancel",
                    title = "Save Changes?",
                    message1 = "Are you sure you want to edit attendance?",
                    message2 = "Once submitted, previous data will be lost.",
                    titleIcon = Icons.AutoMirrored.Filled.Send,
                    rightButtonIcon = Icons.Default.Edit,
                    rightButtonColor = MaterialTheme.colorScheme.secondary
                )
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Header
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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

                // Course Info + Date Picker
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(4.dp)
                            .weight(0.25f)
                    )
                    Card(
                        modifier = Modifier.weight(2.5f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
                        elevation = CardDefaults.cardElevation(22.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = courseCode,
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
                    Box(modifier = Modifier.weight(3f), contentAlignment = Alignment.CenterEnd) {
                        DatePickerField(
                            selectedDate = selectedDate,
                            onDateSelected = { rawDate ->
                                try {
                                    val parsedDate =
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(
                                            rawDate
                                        )
                                    val formattedDate =
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                            parsedDate!!
                                        )
                                    selectedDate = formattedDate
                                    selectedTimestamp.value =
                                        attendanceRecords.value[formattedDate]?.firstOrNull()?.first
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Invalid date selected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    print("$e")
                                }
                            }

                        )

                    }
                }

                if (timestamps.size > 1) {
                    Spacer(modifier = Modifier.height(1.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.4f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val displayTimestamp = selectedTimestamp.value?.let {
                                try {
                                    val millis = it.toLong()
                                    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
                                        Date(
                                            millis
                                        )
                                    )
                                } catch (e: Exception) {
                                    it
                                }
                            } ?: ""

                            OutlinedTextField(
                                value = displayTimestamp,
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
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true,
                            )

                            DropdownMenu(
                                expanded = expanded,
                                offset = DpOffset(18.dp, 3.dp),
                                onDismissRequest = { expanded = false }
                            ) {
                                timestamps.forEach { raw ->
                                    val formatted = try {
                                        val millis = raw.toLong()
                                        SimpleDateFormat(
                                            "hh:mm a",
                                            Locale.getDefault()
                                        ).format(Date(millis))
                                    } catch (e: Exception) {
                                        raw
                                    }

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = formatted,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontFamily = FontFamily(Font(R.font.lobster_regular)),
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        },
                                        onClick = {
                                            selectedTimestamp.value = raw
                                            val entryList =
                                                attendanceRecords.value[selectedDate]?.firstOrNull { it.first == raw }?.second
                                            attendanceState.value = entryList?.toMutableStateList()
                                                ?: mutableStateListOf()
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Attendance Grid
                if (hasAttendance) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        AttendanceReportGrid(
                            attendanceState = attendanceState.value,
                            onToggleAttendance = { updatedEntry ->
                                attendanceState.value = attendanceState.value.map { entry ->
                                    if (entry.uid == updatedEntry.uid) {
                                        entry.copy(isPresent = !entry.isPresent)
                                    } else entry
                                }.toMutableStateList()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Edit & Restore Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.weight(0.25f))
                        Button(
                            onClick = { showEditDialog.value = true },
                            enabled = selectedDocId.value != null,
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onSurface,
                                contentColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text("Edit", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.weight(0.25f))
                        Button(
                            onClick = { showRestoreDialog.value = true },
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onSurface,
                                contentColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text("Restore", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.weight(0.25f))
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Card(
                            modifier = Modifier
                                .fillMaxHeight(0.45f)
                                .fillMaxWidth(0.85f),
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                LottieFromAssets(
                                    assetName = "no_data.json",
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
//                                Text(
//                                    text = "No Attendance data available for the selected Date",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = MaterialTheme.colorScheme.error,
//                                    modifier = Modifier.fillMaxWidth(),
//                                    textAlign = TextAlign.Center
//                                )
                            }
                        }
                    }
                }
            }
        }
    }
}