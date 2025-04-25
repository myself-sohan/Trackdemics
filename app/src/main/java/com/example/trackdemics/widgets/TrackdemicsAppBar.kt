package com.example.trackdemics.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackdemicsAppBar(
    modifier: Modifier = Modifier,
    title: String = "Trackdemics",
    navController: NavController,
    titleContainerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer   ,
    titleTextColor: Color = MaterialTheme.colorScheme.background,
    isEntryScreen: Boolean = true,
    isActionScreen: Boolean = false,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
)
{
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
            if (!isEntryScreen)
            {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
            if(isActionScreen)
            {
                IconButton(
                    onClick = {
                        navController.popBackStack()
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
             IconButton(
                onClick = { /* Add navigation functionality if needed */ },
                modifier = Modifier
                    .size(logoSize)
                    .padding(end = 10.dp)
            )
            {
                Image(
                    painter = painterResource(R.drawable.nitm),
                    contentDescription = "NIT Logo",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}