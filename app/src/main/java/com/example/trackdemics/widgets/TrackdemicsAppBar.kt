package com.example.trackdemics.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackdemicsAppBar(
    modifier: Modifier = Modifier,
    title: String = "Trackdemics",
    navController: NavController,
    isEntryScreen: Boolean = true

)
{
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val logoSize = screenWidth * 0.16f // Dynamically setting logo size as 12% of screen width

    TopAppBar(
        title = {
            Card(
                modifier = Modifier
                    .padding(start = 6.dp, top = 8.dp, bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily(Font(R.font.abril_fatface_regular)),
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        },
        navigationIcon ={
            if (!isEntryScreen)
            {
                Surface {}
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
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    )
}