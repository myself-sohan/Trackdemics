package com.example.trackdemics.screens.role

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.screens.role.components.RoleDropDown
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
                                MaterialTheme.colorScheme.primary.copy(0.7f),
                            )
                        )
                    )
            )
            {
                var expanded = remember { mutableStateOf(false) }
                var selectedRole = remember { mutableStateOf("Choose your Role?") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        //.background(MaterialTheme.colorScheme.background)
                        .padding(top = 100.dp), // Adjusted spacing for alignment
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    RoleDropDown(
                        navController = navController,
                        expanded = expanded,
                        selectedRole = selectedRole
                    )
                }
            }
        }
    }
}