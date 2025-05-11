package com.example.trackdemics.screens.routine

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DomainAdd
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.trackdemics.screens.routine.components.DropdownSelector
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun RoutineScreen(
    navController: NavController,
    viewModel: RoutineViewModel = viewModel()
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

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onSurface,
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                )
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            {
                Spacer(Modifier.height(12.dp))

                // ─── Branch / Semester Row ───────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                )
                {
                    // Branch selector (unchanged)…
                    DropdownSelector(
                        icon = Icons.Default.DomainAdd,
                        label = "Branch",
                        options = viewModel.availableBranches,
                        selectedOption = viewModel.selectedBranch,
                        onOptionSelected = { viewModel.selectedBranch = it },
                        modifier = Modifier
                            .weight(1f)
                    )

                    // Semester selector: parse the trailing number out of "Sem X"
                    DropdownSelector(
                        icon = Icons.Default.School,
                        label = "Semester",
                        options = viewModel.availableSemesters,
                        // display exactly "Sem 6" or "Sem ${selectedSemester}"
                        selectedOption = "Sem ${viewModel.selectedSemester}",
                        onOptionSelected = { semLabel ->
                            // semLabel == "Sem 4" for example
                            val semNumber = semLabel
                                .removePrefix("Sem ")
                                .trim()
                                .toIntOrNull()
                                ?: viewModel.selectedSemester
                            viewModel.selectedSemester = semNumber
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }


                Spacer(Modifier.height(24.dp))

                // ─── Day Chips ────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    days.forEach { day ->
                        val isSelected = viewModel.selectedDay == day
                        AssistChip(
                            onClick = { viewModel.selectedDay = day },
                            label = {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = day.take(3),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (isSelected)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = AssistChipDefaults.assistChipElevation(15.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ─── Outer Surface Boundary for Course List ───────────────
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 6.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
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
                                val timing = viewModel.getTimingForDay(
                                    course.schedule,
                                    viewModel.selectedDay
                                )!!

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Course Code + Name
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Book,
                                                contentDescription = "Course Icon",
                                                modifier = Modifier.size(20.dp)
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
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Place,
                                                    contentDescription = "Location Icon",
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    course.location ?: "TBA",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarToday,
                                                    contentDescription = "Time Icon",
                                                    modifier = Modifier.size(18.dp)
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
}


