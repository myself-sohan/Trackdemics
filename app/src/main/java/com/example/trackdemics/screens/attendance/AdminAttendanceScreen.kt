package com.example.trackdemics.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun AdminAttendanceScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    val branches = listOf("CSE", "ECE")
    val semesters = (1..8).map { it.toString() }

    var selectedBranch by remember { mutableStateOf<String?>(null) }
    var selectedSemester by remember { mutableStateOf<String?>(null) }
    var courseList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    DropdownSelector(
                        label = "Select Branch",
                        options = branches,
                        selectedOption = selectedBranch,
                        onOptionSelected = { selectedBranch = it }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    DropdownSelector(
                        label = "Select Semester",
                        options = semesters,
                        selectedOption = selectedSemester,
                        onOptionSelected = { selectedSemester = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!selectedBranch.isNullOrBlank() && !selectedSemester.isNullOrBlank()) {
                LaunchedEffect(selectedBranch, selectedSemester) {
                    isLoading = true
                    val snapshot = firestore.collection("courses")
                        .whereEqualTo("branch", selectedBranch)
                        .whereEqualTo("semester", selectedSemester)
                        .get()
                        .await()

                    courseList = snapshot.documents.mapNotNull { it.data }
                    isLoading = false
                }

                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    courseList.isEmpty() -> {
                        Text(
                            text = "No courses found for $selectedBranch - Semester $selectedSemester.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(courseList) { course ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(6.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            "${course["code"]} — ${course["name"]}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "Location: ${course["location"] ?: "TBA"}",
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

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption ?: label)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand dropdown")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}
