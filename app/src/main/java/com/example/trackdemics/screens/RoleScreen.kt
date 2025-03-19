package com.example.trackdemics.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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


        }
    }
}