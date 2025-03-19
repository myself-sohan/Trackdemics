package com.example.trackdemics.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.screens.signup.SignUpScreen
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
//                    Text(
//                        text = "Choose your Role?",
//                        style = MaterialTheme.typography.titleLarge.copy(
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.primary
//                        ),
//                        modifier = Modifier
//                            .padding(bottom = 20.dp)
//                    )

                    Box(
                        modifier = Modifier
                            .width(250.dp)
                            .background(MaterialTheme.colorScheme.secondary , shape = MaterialTheme.shapes.medium)
                            .clickable { expanded.value = true }
                            .padding(16.dp)
                    )
                    {
                        Text(
                            text = selectedRole.value,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.inverseOnSurface
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        val roles = listOf("Professor", "Student", "Admin")
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = role,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                },
                                onClick = {
                                    selectedRole.value = role
                                    expanded.value = false
                                    when (role) {
                                        "Professor" -> navController.navigate("SignUpScreen")
                                        "Student" -> navController.navigate("SignUpScreen")
                                        "Admin" -> navController.navigate("LoginScreen")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}