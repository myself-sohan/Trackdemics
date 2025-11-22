package com.example.trackdemics.screens.transport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.trackdemics.screens.transport.components.HeaderArea
import com.example.trackdemics.widgets.TrackdemicsAppBar



data class ScheduleItem(
    val id: Int,
    val time: String,
    val routeTitle: String,
    val routeCodes: String,
    val note: String? = null,
    val day: String = "MON",      // "MON", "TUE", ..., "SAT"
    val inside: Boolean = false, // true => Inside Campus, false => Outside Campus
    val rightLabel: String? = null, // optional small right-side label (e.g. "51/60")
    val status: String? = null      // optional status text (e.g. "Book", "Booking Confirmed", "Bus Full")
)
@Composable
fun BusScheduleScreen(navController: NavController) {
    // Sample data to populate list (mirror the Figma content)
    // --- REPLACE data class and sample data with this ---


// All schedules for all combinations (MON/ SAT × Inside/Outside).
// Fill entries to match the four visual types from your Figma screenshots.
    val allSchedules = remember {
        listOf(
            // --- Monday, OUTSIDE Campus (MON + outside) ----
            ScheduleItem(1, "6:45 AM", "Route 3", "5850   6822", day = "MON", inside = false),
            ScheduleItem(2, "7:00 AM", "Route 2", "4133   6560", day = "MON", inside = false),
            ScheduleItem(3, "7:10 AM", "Route 1", "Not yet updated", day = "MON", inside = false),
            ScheduleItem(4, "7:20 AM", "Route 2", "For Faculties and Officers", note = "Traveller", day = "MON", inside = false),
            ScheduleItem(5, "4:20 PM", "Route 4", "4300   4312", day = "MON", inside = false),

            // --- Monday, INSIDE Campus (MON + inside) ----
            ScheduleItem(6, "8:25 AM", "Route A", "Local Bus 1", day = "MON", inside = true),
            ScheduleItem(7, "9:00 AM", "Route A", "Local Bus 2", day = "MON", inside = true),
            ScheduleItem(8, "9:30 AM", "Route A", "Local Bus 1", day = "MON", inside = true),
            ScheduleItem(9, "10:00 AM", "Route A", "Local Bus 1", day = "MON", inside = true),
            ScheduleItem(10, "10:30 AM", "Route A", "Shillong Bus 1", day = "MON", inside = true),
            ScheduleItem(11, "11:00 AM", "Route A", "Local Bus 2", day = "MON", inside = true),

            // --- Saturday, OUTSIDE Campus (SAT + outside) ----
            ScheduleItem(12, "9:00 AM", "Route 1", "5850   6822", rightLabel = "51/60", status = "Book", day = "SAT", inside = false),
            ScheduleItem(13, "10:00 AM", "Route 1", "4133", rightLabel = "11/30", status = "Booking Confirmed", day = "SAT", inside = false),
            ScheduleItem(14, "5:00 PM", "Route 1", "9059", rightLabel = "36/30", status = "Bus Full", day = "SAT", inside = false),
            ScheduleItem(15, "5:30 PM", "Route 2", "Not yet updated", day = "SAT", inside = false),

            // --- Saturday, INSIDE Campus (SAT + inside) ----
            ScheduleItem(16, "1:00 PM", "Route P", "Local Bus 1", day = "SAT", inside = true),
            ScheduleItem(17, "1:45 PM", "Route S", "Local Bus 2", day = "SAT", inside = true),
            ScheduleItem(18, "7:00 PM", "Route P", "Local Bus 1", day = "SAT", inside = true)
        )
    }



    // UI state
    var selectedDay by remember { mutableStateOf("MON") }
    var insideCampus by remember { mutableStateOf(false) } // false -> outside
    var selectedRoute by remember { mutableStateOf(false)}
    var routineclick by remember { mutableStateOf(false) }
// --- Filter by selectedDay and campus toggle (insideCampus) ---
    val filteredSchedules = remember(allSchedules, selectedDay, insideCampus) {
        allSchedules.filter { it.day.equals(selectedDay, ignoreCase = true) && it.inside == insideCampus }
    }
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
            {
                routineclick = true
            }
            if(routineclick)
            {
                RouteDetailsDialog() {
                    routineclick = false
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // Day tabs and campus toggle
            DayTabsRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CampusToggle(
                inside = insideCampus,
                onToggle = { insideCampus = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Route pill
            RoutePill(
                selectedRoute = selectedRoute,
                inside = insideCampus
            )
            {
                selectedRoute = !selectedRoute
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Schedule list
            // --- REPLACE LazyColumn(schedules) { ... } with this filtered list ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB7E2F5))
            )
            {
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredSchedules) { item ->
                        // We intentionally pass the same ScheduleItem used before;
                        // ScheduleCard uses `time`, `routeTitle`, `routeCodes`, `note` so it continues to work unchanged.
                        ScheduleCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun RouteDetailsDialog(
    routeDetails: List<Pair<String, String>> = defaultRouteDetails(),
    onDismiss: () -> Unit
)
{

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        // Card with thin blue border and rounded corners to match the Figma look.
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 45.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Title area
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
                )
                {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        text = "Route Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                // inside your composable
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .heightIn(max = 360.dp), // allow scrolling when content is long
                    // you can add contentPadding or verticalArrangement if needed
                ) {
                    items(routeDetails) { pair ->
                        val (title, desc) = pair

                        Text(
                            text = title,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = desc,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onSurface ,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

// Close button centered, pill-shaped
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Text(
                            text = "Close",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            }
        }
    }
}

/** Default content that mirrors the screenshot — replace with your real data if needed. */
private fun defaultRouteDetails(): List<Pair<String, String>> = listOf(
    "Route 1" to "Laitumkhrah Police Point, Beat House, Fire Brigade Bus Stop, Nongthymmai, Madanriting, Mawblei and Laitkor.",
    "Route 2" to "Laitumkhrah Police Point, Beat House, Bethany Hospital, Rynjah, Lapalang, Supercare Hospital, Madanriting, Mawblei and Laitkor.",
    "Route 3" to "Mawpat, Langkyrding, Lumshyiap, Golflink, Mawroh, Mawkynroh, NEHU, Kyntonmasser, Mawdabtaki, Motsyiar, Iewrynghep, Jaiaw Langsning, Umshyrpi, Laimer, 12 mer, Mylliem, Sohra",
    "Route 4" to "Mawpat, Langkyrding, Lumshyiap, Golflink, Mawroh, Phudmuri, Mawdabtaki, Motsyiar, Iewrynghep, Jaiaw Langsning, Umshyrpi, Laimer, 12 mer, Mylliem, Sohra",
    "Route A" to "A. Quarter, D. Quarter, PhD Hostel, Boys Hostel, A Block, C Block",
    "Route S" to "A. Quarter, PhD Hostel, Boys Hostel, Admin, Main Gate, Cherapunjee Market",
    "Route P" to "PhD Hostel, Boys Hostel, C Block"
)

@Composable
private fun DayTabsRow(selectedDay: String, onDaySelected: (String) -> Unit) {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        for (d in days) {
            val isSelected = d == selectedDay
            Card(
                modifier = Modifier
                    .width(55.dp)
                    .height(35.dp)
                    .clickable { onDaySelected(d) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(15.dp),
                border = BorderStroke(
                    1.dp,
                    color = Color.Gray
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = d,
                        color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



@Composable
private fun CampusToggle(
    inside: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val primaryBlue = Color(0xFF0F517D)
    val lightBg = Color(0xFFE9F4FB)
    val unselectedBorder = Color(0xFFDDEAF3)

    // asymmetrical shapes: left has big rounding on the end, right has big rounding on the start
    val leftShape = RoundedCornerShape(
        topStart = 0.dp, bottomStart = 30.dp,
        topEnd = 30.dp, bottomEnd = 0.dp)
    val rightShape = RoundedCornerShape(
        topStart = 30.dp, bottomStart = 0.dp,
        topEnd = 0.dp, bottomEnd = 30.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // Inside campus surface
        Surface(
            modifier = Modifier
                .weight(0.5f)
                .height(56.dp)
                .clickable { onToggle(true) },
            shape = leftShape,
            color =
                if (inside) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.25.dp, color = MaterialTheme.colorScheme.onSurface),
            tonalElevation = 20.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Inside\nCampus",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    color = if (inside) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Outside campus surface
        Surface(
            modifier = Modifier
                .weight(0.5f)
                .height(56.dp)
                .clickable { onToggle(false) },
            shape = rightShape,
            color =
                if (inside) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.onPrimaryContainer,
            border = BorderStroke(1.25.dp, color = MaterialTheme.colorScheme.onSurface),
            tonalElevation = 20.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Outside\nCampus",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    color = if (inside) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}


@Composable
private fun RoutePill(
    selectedRoute: Boolean,
    inside: Boolean,
    onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    )
    {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = if(selectedRoute && !inside)
                        "NITM Sohra to Shillong"
                       else if(!selectedRoute && !inside)
                        "Shillong to NITM Sohra"
                       else if(!selectedRoute && inside)
                        "Towards Living Quarters"
                       else
                        "From Living Quarters",
                style = MaterialTheme.typography.titleMedium    ,
                fontWeight = FontWeight.SemiBold,

                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "swap",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
private fun ScheduleCard(item: ScheduleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Centered time
            Text(
                text = item.time,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "pin",
                    tint = Color(0xFF0F517D),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(text = item.routeTitle, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    // If routeCodes includes "Not yet updated" display it as muted text
                    if (item.routeCodes.contains("Not yet updated", ignoreCase = true)) {
                        Text(text = item.routeCodes, color = Color.Gray, fontStyle = FontStyle.Italic)
                    } else {
                        Text(text = item.routeCodes, style = MaterialTheme.typography.bodySmall)
                    }
                    // optional extra note in red
                    if (!item.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = item.note, color = Color(0xFFB00020), fontStyle = FontStyle.Italic)
                    }
                }
            }
        }
    }
}
