package com.example.trackdemics.screens.login

import WelcomeText
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.navigation.TrackdemicsScreens
import com.example.trackdemics.screens.login.components.LoginForm
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun LoginScreen(
    navController: NavController,
    role: String = "admin",
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    var loading by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler(enabled = true) {
        navController.navigate(TrackdemicsScreens.RoleScreen.name)
    }
    LaunchedEffect(loginState) {
        loginState?.let { result ->
            loading = false
            viewModel.clearLoginState()
            result.fold(
                onSuccess = {
                    when (role.uppercase()) {
                        "STUDENT" -> navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                        "PROFESSOR" -> navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
                        "ADMIN" -> navController.navigate(TrackdemicsScreens.AdminHomeScreen.name)
                    }
                },
                onFailure = { error ->
                    Toast.makeText(context, error.message ?: "Login failed", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.clearLoginState()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(onBackClick = {
                navController.navigate(TrackdemicsScreens.RoleScreen.name)
            })
        }
    )
    {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(1f),
                                MaterialTheme.colorScheme.primary.copy(0.7f)
                            )
                        )
                    )
            )
            {
                Column(modifier = Modifier.fillMaxSize())
                {

                    // Top Greeting Text
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 16.dp, top = 48.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        WelcomeText(
                            greet = "Welcome",
                            role = role
                        )
                    }


                    Spacer(modifier = Modifier.height(30.dp))

                    // Bottom Card Section
                    Surface(
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {

                            Spacer(modifier = Modifier.height(24.dp))

                            // Login form
                            LoginForm(
                                loading = loading,
                                role = role
                            ) { email, password ->
                                loading = true
                                viewModel.login(email, password, role)
                            }


                            Spacer(modifier = Modifier.height(40.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        bottom = 2.dp,
                                        end = 24.dp
                                    ), // adjust bottom padding to match your design
                                contentAlignment = Alignment.BottomEnd
                            )
                            {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = if (role == "ADMIN") "Not an admin?" else "Don't have an account?",
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Text(
                                        text = if (role == "ADMIN") "Go Back" else "Sign Up",
                                        modifier = Modifier
                                            .padding(top = 2.dp)
                                            .clickable {
                                                if (role == "ADMIN")
                                                    navController.navigate(TrackdemicsScreens.RoleScreen.name)
                                                else
                                                    navController.navigate(TrackdemicsScreens.SignUpScreen.name + "/$role")
                                            },
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontFamily = FontFamily(Font(R.font.lobster_regular)),
                                        letterSpacing = 1.2.sp
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}


