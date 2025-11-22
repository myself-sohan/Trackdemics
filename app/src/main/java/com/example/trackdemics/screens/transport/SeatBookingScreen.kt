package com.example.trackdemics.screens.transport

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.R
import com.example.trackdemics.screens.transport.components.ConfirmationDialog
import com.example.trackdemics.widgets.PasswordVisibility
import com.example.trackdemics.widgets.SubmitButton
import java.net.URLDecoder

/**
 * Corrected SeatBookingScreen
 *
 * - Accepts bus data via nav-arguments (busId, dateTime, route, busCode, seatsTaken, capacity, isBooked)
 * - First Name, Last Name and Role fields are prefilled and read-only (disabled) as per design.
 * - Roll Number and Phone Number are editable and required.
 * - Confirm button disabled until both Roll Number and Phone Number are filled.
 * - On Confirm: computes newSeats = (seatsTaken + 1).coerceAtMost(capacity),
 *   pops this screen and navigates to SpecialBusScreen with result args:
 *     SpecialBusScreen/{bookedBusId}/{updatedSeats}
 *
 * Note: This file assumes your navigation graph contains
 * - a SeatBookingScreen route that provides these arguments,
 * - a SpecialBusScreen route that accepts (bookedBusId, updatedSeats) as args.
 */
@Composable
fun SeatBookingScreen(
    navController: NavController,
    busId: Int,
    dateTime: String,
    route: String,
    busCode: String,
    seatsTaken: Int,
    capacity: Int,
    isBooked: Boolean
) {
    // --- Form state ---
    // Pre-filled and read-only fields per your design
    var firstName by remember { mutableStateOf("SOHAN") }
    var lastName by remember { mutableStateOf("SAHA") }
    var role by remember { mutableStateOf("STUDENT") }

    // Editable required fields
    var rollNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Confirm enabled only when both required fields are non-empty
    val canConfirm by derivedStateOf { rollNumber.isNotBlank() && phoneNumber.isNotBlank() }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = { navController.popBackStack() },
                isScheduleScreen = false,
                isActionScreen = true,
                logoimage = painterResource(R.drawable.img_profile),
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {
            if(showDialog)
            {
                ConfirmationDialog(
                    title = "Confirm Booking",
                    message1 = "Are you sure you want to book this bus?",
                    rightButtonColor = MaterialTheme.colorScheme.primary,
                    onDismissRequest = { showDialog = false },
                    onConfirm = {
                         //compute updated seats and navigate back to SpecialBusScreen with args
                                    val newSeats = (seatsTaken + 1).coerceAtMost(capacity)

                                    // pop this booking screen
                                    navController.popBackStack()

                                    // navigate to SpecialBusScreen with bookedBusId and updatedSeats
                                    navController.navigate("${TrackdemicsScreens.SpecialBusScreen.name}/$busId/$newSeats")
                    }
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 23.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            )
            {


                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                )
                {
                    // Overlapping pill - Book Your Seat!
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-22).dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF6F9FB))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // bus icon (using android built-in as placeholder)
                            Icon(
                                imageVector = Icons.Filled.AirlineSeatReclineExtra,
                                contentDescription = "bus",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "Book Your Seat!",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    // Date/time and route text below the pill (use passed-in args)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp, start = 12.dp, end = 12.dp, bottom = 30.dp),
                    ) {
                        val displayDateTime = try {
                            URLDecoder.decode(dateTime, "UTF-8")
                        } catch (e: Exception) {
                            dateTime.replace("+", " ")
                        }
                        val displayRoute = try {
                            URLDecoder.decode(route, "UTF-8")
                        } catch (e: Exception) {
                            route.replace("+", " ")
                        }
                        Text(
                            text = displayDateTime,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.weight(1.2f)
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.8f),
                        )
                        {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Pin",
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = displayRoute,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.surface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Content card area where form sits
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .background(Color(0xFFB7E2F5)),
                    //.padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
                {
                    val ph_numberFocusRequest = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    Spacer(modifier = Modifier.height(50.dp))
                    // First Name (read-only)
                    SmallLabeledField(
                        label = "First Name",
                        value = firstName,
                        onValueChange = { /* disabled */ },
                        enabled = false,
                        hint = ""
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Last Name (read-only)
                    SmallLabeledField(
                        label = "Last Name",
                        value = lastName,
                        onValueChange = { /* disabled */ },
                        enabled = false,
                        hint = ""
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Role (read-only)
                    SmallLabeledField(
                        label = "Role",
                        value = role,
                        onValueChange = { /* disabled */ },
                        enabled = false,
                        hint = ""
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Roll Number - editable, required
                    SmallLabeledField(
                        label = "Roll Number",
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        enabled = true,
                        hint = "B22CS034",
                        onAction = KeyboardActions {
                            ph_numberFocusRequest.requestFocus()
                        },
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Phone Number - editable, required
                    SmallLabeledField(
                        modifier = Modifier
                            .focusRequester(ph_numberFocusRequest),
                        label = "Phone Number",
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        enabled = true,
                        hint = "+91 8420538331",
                        keyboardType = KeyboardType.Number,
                        onAction = KeyboardActions { keyboardController?.hide() }
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SubmitButton(
                            textId = "Confirm",
                            loading = !canConfirm,
                        ) {
                            showDialog = true
                        }
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(50.dp)
//                                .clip(RoundedCornerShape(28.dp))
//                                .background(Color(0xFFBFCFDFF))
//                                .clickable(enabled = canConfirm) {
//                                    // compute updated seats and navigate back to SpecialBusScreen with args
//                                    val newSeats = (seatsTaken + 1).coerceAtMost(capacity)
//
//                                    // pop this booking screen
//                                    navController.popBackStack()
//
//                                    // navigate to SpecialBusScreen with bookedBusId and updatedSeats
//                                    navController.navigate("${TrackdemicsScreens.SpecialBusScreen.name}/$busId/$newSeats")
//                                },
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "Confirm Booking",
//                                color = if (canConfirm) Color.White else Color(0xFF6B7280),
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 16.sp
//                            )
//                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

/**
 * SmallLabeledField:
 * - label above in italic small text (matches Figma)
 * - white rounded surface with slight elevation and a transparent TextField inside
 * - parameter `enabled` controls editability
 */
@Composable
private fun SmallLabeledField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default ,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Ascii,
    hint: String = ""
) {
    Card(
        modifier = Modifier.padding(horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(35.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
    {
        OutlinedTextField(
            value = value,
            maxLines = 1,
            keyboardActions = onAction,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            onValueChange = { newValue -> onValueChange.invoke(newValue) },
            label ={
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            textStyle = TextStyle(
                color = if(enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                fontFamily = FontFamily(Font(R.font.notosans_variablefont))
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = Color.Blue
            ),
            modifier = modifier
                .padding( start = 3.dp, end = 3.dp, bottom = 2.dp, top = 2.dp)
                //.height(70.dp)
                .fillMaxWidth(),
            enabled = enabled,
        )
    }
}
