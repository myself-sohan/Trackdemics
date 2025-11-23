package com.example.trackdemics.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import com.example.trackdemics.utils.LoadImageWithGlide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackdemicsAppBar(
    modifier: Modifier = Modifier,
    title: String = "Trackdemics",
    onBackClick: () -> Unit,
    titleContainerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    titleTextColor: Color = MaterialTheme.colorScheme.background,
    isEntryScreen: Boolean = true,
    isActionScreen: Boolean = false,
    isScheduleScreen: Boolean = false,
    isSeatBookingScreen: Boolean = false,
    logoimage: Painter = painterResource(R.drawable.nitm),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    role: String = "STUDENT"
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val user = auth.currentUser
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val logoSize = screenWidth * 0.16f // Dynamically setting logo size as 12% of screen width

    val coroutineScope = rememberCoroutineScope()

    val userRole = when (role) {
        "STUDENT" -> "students"
        "ADMIN" -> "admin"
        "PROFESSOR" -> "professors"
        else -> ""
    }

    LaunchedEffect(Unit) {
        auth.currentUser?.email?.let { email ->
            val normalizedEmail = email.trim().lowercase()
            val snapshot = firestore.collection(userRole)
                .whereEqualTo("email", normalizedEmail)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            profileImageUrl = doc?.getString("profile_pic_url")
        }
    }

    TopAppBar(
        title = {
            Card(
                modifier = Modifier
                    .padding(start = 6.dp, top = 8.dp, bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = titleContainerColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = titleTextColor
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        },
        navigationIcon = {
            if (!isEntryScreen) {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
            if (isActionScreen) {
                IconButton(
                    onClick = {
                        onBackClick()
                    },
                )
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow",
                    )
                }
            }
        },
        actions = {
            when {
                (!isScheduleScreen && !isSeatBookingScreen) -> {
                    IconButton(
                        onClick = { /* Add navigation functionality if needed */ },
                        modifier = Modifier
                            .size(logoSize)
                            .padding(end = 10.dp)
                    )
                    {
                        Image(
                            painter = logoimage,
                            contentDescription = "NIT Logo",
                        )
                    }
                }

                (isSeatBookingScreen) -> {
                    LoadImageWithGlide(
                        imageUrl = profileImageUrl ?: "",
                        modifier = Modifier
                            .size(logoSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary, CircleShape)
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                }

                else ->
                    OutlinedButton(
                        modifier = Modifier
                            .height(30.dp)
                            .padding(end = 20.dp),
                        onClick = { /* open help */ },
                        border = BorderStroke(
                            color = titleContainerColor,
                            width = 1.dp
                        ),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Need Help?",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Serif,
                                color = titleContainerColor
                            )
                        )
                    }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}