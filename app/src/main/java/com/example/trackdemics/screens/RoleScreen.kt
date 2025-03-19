package com.example.trackdemics.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun RoleScreen(
    navController: NavController
)
{
    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                navController = navController
            )
        }
    )
    {
        Surface(
            modifier = Modifier.padding(it)
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                MaterialTheme.colorScheme.primaryContainer,
                                //MaterialTheme.colorScheme.primary,
                            )
                        )
                    )
            )
            {

            }
        }
    }
}