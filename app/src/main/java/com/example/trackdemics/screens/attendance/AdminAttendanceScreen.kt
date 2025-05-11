package com.example.trackdemics.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.example.trackdemics.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.attendance.components.AddCourseCard
import com.example.trackdemics.screens.attendance.components.AdminAttendanceCard
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun AdminAttendanceScreen(
    navController: NavController
)
{
    val firestore = FirebaseFirestore.getInstance()
    val branches = listOf("CSE", "CE")
    val semesters = (1..8).map { it.toString() }

    var selectedBranch by remember { mutableStateOf<String?>(null) }
    var selectedSemester by remember { mutableStateOf<String?>(null) }
    var courseList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    navController.navigate(TrackdemicsScreens.AdminHomeScreen.name)
                },
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.inversePrimary,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            )
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(22.dp))
                {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    )
                    {
                        DropDownSelector(
                            label = "Branch",
                            options = branches,
                            selectedOption = selectedBranch,
                            onOptionSelected = { selectedBranch = it }
                        )
                    }
                     Box(
                         modifier = Modifier
                             .weight(1f)
                     )
                     {
                        DropDownSelector(
                            label = "Semester",
                            options = semesters,
                            selectedOption = selectedSemester,
                            onOptionSelected = { selectedSemester = it }
                        )
                    }
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
                    isLoading ->
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        )
                        {
                            CircularProgressIndicator()
                        }
                    }
                    courseList.isEmpty() ->
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Card(
                                modifier = Modifier
                                    .fillMaxHeight(0.1f)
                                    .fillMaxWidth(0.5f),
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(44.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            )
                            {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No Courses Found",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    else ->
                    {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp))
                        {
                            items(courseList)
                            { course ->
                                AdminAttendanceCard(
                                    course = course,
                                    coroutineScope = coroutineScope,
                                    navController = navController,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DropDownSelector(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
)
{
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        AddCourseCard(
            label = label,
            onClick = { expanded = true },
            selectedOption = selectedOption,
            imageVector = Icons.Default.ArrowDropDown,
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth(0.95f)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shadowElevation = 90.dp,
            containerColor = MaterialTheme.colorScheme.onSurface,
            offset = DpOffset(25.dp,-25.dp)
        )
        {
            options.forEach{ option ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Arrow Right",
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(15.dp)
                        )
                    },
                    text = {
                        Text(
                            text = option,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}


