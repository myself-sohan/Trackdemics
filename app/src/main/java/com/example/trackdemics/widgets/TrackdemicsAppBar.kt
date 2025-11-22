package com.example.trackdemics.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import kotlinx.coroutines.launch

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
    logoimage: Painter = painterResource(R.drawable.nitm),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val logoSize = screenWidth * 0.16f // Dynamically setting logo size as 12% of screen width
    val coroutineScope = rememberCoroutineScope()

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
            if(!isScheduleScreen)
            {
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
            else{
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