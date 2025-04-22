package com.example.trackdemics.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.login.components.LoginForm
import com.example.trackdemics.widgets.WelcomeText
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun LoginScreen(
    navController: NavController,
    role: String = "admin"
)
{
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
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
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {
                    WelcomeText(
                        greet = "Welcome",
                        role = role
                    )
                    LoginForm(
                        loading = false,
                        role = role
                    )
                    {
                        when(role)
                        {
                            "STUDENT"-> navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                            "PROFESSOR"-> navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
                            "ADMIN"-> navController.navigate(TrackdemicsScreens.AdminHomeScreen.name)
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
                        val text = if(role == "ADMIN") "Go Back" else "Sign Up"
                        val message = if(role == "ADMIN") "Not an admin?" else "New User?"
                        Text(
                            text = message
                        )
                        Text(
                            text = text,
                            modifier = Modifier.clickable{
                                if(role == "ADMIN")
                                    navController.navigate(TrackdemicsScreens.RoleScreen.name)
                                else
                                    navController.navigate(TrackdemicsScreens.SignUpScreen.name+"/$role")
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

