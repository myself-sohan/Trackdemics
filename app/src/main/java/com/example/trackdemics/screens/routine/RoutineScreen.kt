package com.example.trackdemics.screens.routine

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DomainAdd
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.screens.routine.components.DropdownSelector
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun RoutineScreen(
    navController: NavController,
    viewModel: RoutineViewModel = viewModel()
) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

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
                        onOptionSelected = { viewModel.onBranchChanged(it) },
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
                            val sem = semLabel.removePrefix("Sem ").toIntOrNull()
                                ?: return@DropdownSelector
                            viewModel.onSemesterChanged(sem)
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
                    shadowElevation = 56.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                {
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
                            verticalArrangement = Arrangement.spacedBy(25.dp),
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxSize()
                        )
                        {
                            items(courses.size) { idx ->
                                val course = courses[idx]
                                // we know timing != null after filtering
                                val timing = viewModel.getTimingForDay(
                                    course.schedule,
                                    viewModel.selectedDay
                                )!!

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(56.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    colors = CardDefaults.cardColors(
                                        MaterialTheme.colorScheme.surface
                                    ),
                                    border = BorderStroke(
                                        color = MaterialTheme.colorScheme.primary,
                                        width = 1.dp
                                    )
                                )
                                {
                                    Column(modifier = Modifier.padding(20.dp))
                                    {
                                        // Course Code + Name
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        )
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Book,
                                                contentDescription = "Course Icon",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(Modifier.width(18.dp))
                                            Text(
                                                text = "${course.code} — ${course.name}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                                            )
                                        }

                                        Spacer(Modifier.height(16.dp))

                                        // Location & Time
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        )
                                        {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Place,
                                                    contentDescription = "Location Icon",
                                                    modifier = Modifier.size(22.dp)
                                                )
                                                Spacer(Modifier.width(10.dp))
                                                Text(
                                                    course.location ?: "TBA",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                                                )
                                            }
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.AccessTimeFilled,
                                                    contentDescription = "Time Icon",
                                                    modifier = Modifier.size(22.dp)
                                                )
                                                Spacer(Modifier.width(10.dp))
                                                Text(
                                                    "${timing.startTime} – ${timing.endTime}",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontFamily = FontFamily(Font(R.font.notosans_variablefont))
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


