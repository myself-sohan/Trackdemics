package com.example.trackdemics.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.trackdemics.widgets.WelcomeText
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun AdminHomeScreen(
    navController: NavController,
)
{
    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                navController = navController,
            )
        }
    )
    {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                MaterialTheme.colorScheme.primary.copy(0.7f),
                            )
                        )
                    )
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {
                    WelcomeText(
                        greet = "Let's  Start",
                        role = "Admin"
                    )
                }
            }
        }
    }
}