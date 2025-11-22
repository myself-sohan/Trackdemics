package com.example.trackdemics.screens.transport

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.transport.components.ConfirmationDialog
import com.example.trackdemics.widgets.TrackdemicsAppBar

/**
 * Figma reference image (local path): /mnt/data/f56add14-3015-4de3-ac7d-91bcf0848040.png
 *
 * Drop-in screen that reproduces the "Special Buses" screen layout and provides
 * a reusable LazyColumn for bus items. The header uses a vertical gradient,
 * the centered pill overlaps the gradient (half above / half inside), and the
 * list items are white rounded cards as in your design.
 */

data class SpecialBusItem(
    val id: Int,
    val dateTime: String,
    val route: String,
    val busCode: String,
    val seatsTaken: Int,
    val capacity: Int
) {
    // helper to show the string like "10/30"
    fun seatsText() = "${seatsTaken}/${capacity}"
}

@Composable
fun SpecialBusScreen(
    navController: NavController,
    bookedBusId: Int? = null,
    updatedSeats: Int? = null,
    role: String
) {
    // sample data - you can replace / append more items to this list
    val busesState = remember {
        mutableStateListOf(
            SpecialBusItem(
                id = 1,
                dateTime = "SUN, 23/11/2025, 10:00 AM",
                route = "Shillong to NITM Sohra",
                busCode = "4133",
                seatsTaken = 10,
                capacity = 30
            ),
            SpecialBusItem(
                id = 2,
                dateTime = "THU, 29/11/2025, 2:00 PM",
                route = "NITM Sohra to Shillong",
                busCode = "4210",
                seatsTaken = 4,
                capacity = 30
            )
            // add more items here
        )
    }
    val showDialog = remember { mutableStateOf(false) }
    val cancelingIndex = remember { mutableStateOf(-1) }
    // Map to hold which bus (by id) is currently booked by the user in this session
    val bookedMap = remember { mutableStateMapOf<Int, Boolean>() }
    LaunchedEffect(key1 = bookedBusId, key2 = updatedSeats) {
        if (bookedBusId != null && updatedSeats != null) {
            val index = busesState.indexOfFirst { it.id == bookedBusId }
            if (index >= 0) {
                val current = busesState[index]
                busesState[index] = current.copy(seatsTaken = updatedSeats)
                bookedMap[bookedBusId] = true
            }
        }
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = { navController.navigate("BusScheduleScreen/$role") },
                isScheduleScreen = true,
                isActionScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
        {
            if (showDialog.value) {
                ConfirmationDialog(
                    onDismissRequest = {
                        showDialog.value = false
                        cancelingIndex.value = -1
                    },
                    onConfirm = {
                        // user confirmed cancellation: undo booking for the selected bus
                        if (cancelingIndex.value >= 0 && cancelingIndex.value < busesState.size) {
                            val current = busesState[cancelingIndex.value]
                            // decrement seatsTaken safely
                            busesState[cancelingIndex.value] = current.copy(
                                seatsTaken = (current.seatsTaken - 1).coerceAtLeast(0)
                            )
                            // mark unbooked
                            bookedMap[current.id] = false
                        }
                        showDialog.value = false
                        cancelingIndex.value = -1
                    }
                )
            }

            // Header gradient card (rounded bottom corners)
            val headerGradient = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.inversePrimary
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(headerGradient)
            )


            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 0.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFF6F9FB))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // bus icon (using android built-in as placeholder)
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = "bus",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Special Bus",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }


            // Content area that starts below the pill / header
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp) // push content below header & pill
            ) {
                // A small white rounded container that visually separates the first card (optional)
                // We use a LazyColumn for the list of buses
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    itemsIndexed(busesState) { index, bus ->
                        val isBooked = bookedMap[bus.id] == true
                        SpecialBusCard(
                            bus = bus,
                            isBooked = isBooked,
                            onBookClicked = {
                                // Book: mark in bookedMap and increase seatsTaken by 1
                                // Cancel: unmark and decrease seatsTaken by 1 (not below 0)
                                val encodedDate = java.net.URLEncoder.encode(bus.dateTime, "utf-8")
                                val encodedRoute = java.net.URLEncoder.encode(bus.route, "utf-8")
                                val route =
                                    "${TrackdemicsScreens.SeatBookingScreen.name}/${bus.id}/$encodedDate/$encodedRoute/${bus.busCode}/${bus.seatsTaken}/${bus.capacity}/false/$role"
                                navController.navigate(route)
                            },
                            onCancelClicked = {
                                // Cancel: unmark and decrease seatsTaken by 1 (not below 0)
                                showDialog.value = true
                                cancelingIndex.value = index
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecialBusCard(
    bus: SpecialBusItem,
    isBooked: Boolean,
    onBookClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isBooked) 130.dp else 110.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 12.dp),
        )
        {
            Text(
                text = bus.dateTime,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                // Left column: date/time + route + code
                Column(modifier = Modifier.weight(1f))
                {

                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "pin",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bus.route,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = bus.busCode,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Right column: seats info and Book action
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = bus.seatsText(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (!isBooked) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onBookClicked() }
                        )
                        {
                            Text(
                                text = "Book",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = "open",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onCancelClicked() }
                        )
                        {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            if (isBooked) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.inversePrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = "Booking Confirmed",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
