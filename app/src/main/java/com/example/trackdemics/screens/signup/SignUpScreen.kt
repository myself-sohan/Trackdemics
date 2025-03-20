package com.example.trackdemics.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.signup.components.SignUpForm
import com.example.trackdemics.widgets.WelcomeText
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun SignUpScreen(
    navController: NavController,
    role: String
) {
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
                        greet = "Get Started",
                        role = role
                    )
                    SignUpForm(
                        role = role,
                        loading = false
                    )
                    {
                        when (role) {
                            "STUDENT" -> navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                            "PROFESSOR" -> navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(15.dp)
                    )
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = "Already Have an Account?"
                        )
                        Text(
                            text = "Log In",
                            modifier = Modifier.clickable{
                                    navController.navigate(TrackdemicsScreens.LoginScreen.name+"/$role")
                            }
                                .padding(start = 5.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

