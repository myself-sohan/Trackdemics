package com.example.trackdemics.screens.transport


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.screens.transport.components.HeaderArea
import com.example.trackdemics.widgets.TrackdemicsAppBar



@Composable
fun SpecialBusScreen(navController: NavController) {
    // Sample data to populate list (mirror the Figma content)
    val schedules = remember {
        listOf(
            ScheduleItem(1, "6:45 AM", "Route 3", "5850   6822"),
            ScheduleItem(2, "7:00 AM", "Route 2", "4133   6560"),
            ScheduleItem(3, "7:10 AM", "Route 1", "Not yet updated"),
            ScheduleItem(4, "7:20 AM", "Route 2", "For Faculties and Officers", "Traveller"),
            ScheduleItem(5, "4:20 PM", "Route 4", "4300   4312")
        )
    }

    // UI state
    var selectedDay by remember { mutableStateOf("MON") }
    var insideCampus by remember { mutableStateOf(false) } // false -> outside
    var selectedRoute by remember { mutableStateOf("NITM Sohra to Shillong") }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    navController.popBackStack()
                },
                isScheduleScreen = true,
                isActionScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background
            )
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            //.background(Color(0xFFF4F6F8))
        ) {
            // Gradient header area
            HeaderArea(
                navController = navController
            )

        }
    }
}