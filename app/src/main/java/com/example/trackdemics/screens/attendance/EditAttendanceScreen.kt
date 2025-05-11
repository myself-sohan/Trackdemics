package com.example.trackdemics.screens.attendance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.example.trackdemics.screens.attendance.components.AttendanceReportGrid
import com.example.trackdemics.screens.attendance.components.ConfirmationDialog
import com.example.trackdemics.screens.attendance.components.DatePickerField
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun EditAttendanceScreen(
    navController: NavController
)
{
    var selectedDate by remember { mutableStateOf("01/04/2025") }
    val dates = listOf("01/04/2025", "03/04/2025", "05/04/2025")
    var selectedTimestamp by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }


    val dummyAttendanceRecords = remember {
        val students = List(40) { index ->
            val roll = "B22CS%03d".format(index + 1)
            val name = "Student ${index + 1}"
            Student(roll, name)
        }

        listOf(
            AttendanceRecord(
                date = "01/04/2025",
                timestamp = "09:00 AM",
                attendance = students.map {
                    AttendanceEntry(it, listOf(true, false).random())
                }
            ),
            AttendanceRecord(
                date = "03/04/2025",
                timestamp = "09:00 AM",
                attendance = students.map {
                    AttendanceEntry(it, listOf(true, false).random())
                }
            ),
            AttendanceRecord(
                date = "03/04/2025",
                timestamp = "02:00 PM",
                attendance = students.map {
                    AttendanceEntry(it, listOf(true, false).random())
                }
            ),
            AttendanceRecord(
                date = "05/04/2025",
                timestamp = "09:00 AM",
                attendance = students.map {
                    AttendanceEntry(it, listOf(true, false).random())
                }
            )
        )
    }
// ✅ Pull records for selected date and timestamps
    val recordsForDate = dummyAttendanceRecords.filter { it.date == selectedDate }
    val timestamps = recordsForDate.map { it.timestamp }
    var attendanceState by remember {
        mutableStateOf(
            recordsForDate.first().attendance.toMutableStateList()
        )
    }
    val context = LocalContext.current
    val originalState = remember(attendanceState) { attendanceState.map { it.copy() } }
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
            if(showRestoreDialog.value)
            {
                if (showRestoreDialog.value) {
                    ConfirmationDialog(
                        courseCode = null,
                        onDismissRequest = { showRestoreDialog.value = false },
                        onConfirm = {
                            showRestoreDialog.value = false

                            // Reset the selected students to original data
                            attendanceState = originalState.toMutableStateList()

                            // Optional: Show a Toast
                            Toast.makeText(context, "Attendance restored to Original.", Toast.LENGTH_SHORT).show()
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
            if(showEditDialog.value)
            {
                ConfirmationDialog(
                    courseCode = null,
                    onDismissRequest = { showEditDialog.value = false },
                    onConfirm = {
                        showEditDialog.value = false
                        // ⬇️ Your submit logic here:
                        //println("Submitting data: ${selectedCards.value}")
                        Toast.makeText(context, "Attendance Report Editted Successfully", Toast.LENGTH_SHORT).show()
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
                                dummyAttendanceRecords.firstOrNull { it.date == date }?.let {
                                    attendanceState = it.attendance.toMutableStateList()

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
                // 🆕 INSERT HERE: Below the DatePickerField, add the dropdown
                if (timestamps.size > 1)
                {
                    selectedTimestamp = timestamps.first()
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
                                value = selectedTimestamp!!,
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
                                            selectedTimestamp = time
                                            expanded = false
                                            recordsForDate.find { it.timestamp == time }?.let {
                                                attendanceState = it.attendance.toMutableStateList()
                                            }
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
                        attendanceState = attendanceState,
                        onToggleAttendance = { entry ->
                            val index = attendanceState.indexOf(entry)
                            attendanceState[index] = entry.copy(isPresent = !entry.isPresent)
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
                    Button(onClick = {
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

data class Student(val rollNumber: String, val name: String)
data class AttendanceEntry(val student: Student, val isPresent: Boolean)
data class AttendanceRecord(val date: String, val timestamp: String, val attendance: List<AttendanceEntry>)
