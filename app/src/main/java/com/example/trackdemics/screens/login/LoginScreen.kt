package com.example.trackdemics.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController
)
{
    Surface(
        color = MaterialTheme.colorScheme.background
    )
    {
        Column(
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "Login Screen",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}