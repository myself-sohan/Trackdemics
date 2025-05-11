package com.example.trackdemics.screens.routine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun RoutineScreen(
    navController: NavController,
    viewModel: RoutineViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday")

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {navController.popBackStack()},
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // ─── Branch / Semester Row ───────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DropdownSelector(
                    icon            = Icons.Default.Class,
                    label           = "Branch",
                    options         = viewModel.availableBranches,
                    selectedOption  = viewModel.selectedBranch,
                    onOptionSelected= { viewModel.selectedBranch = it },
                    modifier        = Modifier.weight(1f)
                )
                DropdownSelector(
                    icon            = Icons.Default.CalendarToday,
                    label           = "Semester",
                    options         = viewModel.availableSemesters.map { it.toString() },
                    selectedOption  = viewModel.selectedSemester.toString(),
                    onOptionSelected= { viewModel.selectedSemester = it.toInt() },
                    modifier        = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ─── Day Chips ────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    val isSelected = viewModel.selectedDay == day
                    AssistChip(
                        onClick = { viewModel.selectedDay = day },
                        label   = { Text(day.take(3)) },
                        colors  = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = if (isSelected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ─── Outer Surface Boundary for Course List ───────────────
            Surface(
                shape          = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp,
                color          = MaterialTheme.colorScheme.surfaceVariant,
                modifier       = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val courses = viewModel.getFilteredCourses()

                if (courses.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No classes scheduled",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(courses.size) { idx ->
                            val course = courses[idx]
                            // we know timing != null after filtering
                            val timing = viewModel.getTimingForDay(course.schedule, viewModel.selectedDay)!!

                            Card(
                                shape          = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                modifier       = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    // Course Code + Name
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector    = Icons.Default.Book,
                                            contentDescription = "Course Icon",
                                            modifier       = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "${course.code} — ${course.name}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    // Location & Time
                                    Row(
                                        modifier            = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                                        verticalAlignment     = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector    = Icons.Default.Place,
                                                contentDescription = "Location Icon",
                                                modifier       = Modifier.size(18.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                course.location ?: "TBA",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector    = Icons.Default.CalendarToday,
                                                contentDescription = "Time Icon",
                                                modifier       = Modifier.size(18.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                "${timing.startTime} – ${timing.endTime}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * A dropdown selector with an icon prefix and arrow indicator.
 * `modifier` allows you to weight it in a Row.
 */
@Composable
fun DropdownSelector(
    icon: ImageVector,
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedButton(
            onClick  = { expanded = true },
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(icon, contentDescription = null, Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(selectedOption, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector    = Icons.Default.ArrowDropDown,
                contentDescription = "Expand",
                modifier       = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
            modifier         = Modifier.fillMaxWidth(0.9f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text    = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
