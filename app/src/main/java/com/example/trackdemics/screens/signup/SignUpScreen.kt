package com.example.trackdemics.screens.signup

import WelcomeText
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.trackdemics.screens.signup.components.SignUpForm
import com.example.trackdemics.widgets.TrackdemicsAppBar
//import com.example.trackdemics.widgets.WelcomeText

@Composable
fun SignUpScreen(
    navController: NavController,
    role: String,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val signUpState by viewModel.signUpState.collectAsState()
    var loading by remember { mutableStateOf(false) }
    var resultHandled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(signUpState) {
        if (signUpState != null && !resultHandled) {
            resultHandled = true // prevent re-handling

            signUpState!!.fold(
                onSuccess = {
                    loading = false
                    viewModel.clearSignUpState()

                    Log.d("SIGNUP_SUCCESS", "Sign-up succeeded")
                    when (role.uppercase()) {
                        "STUDENT" -> navController.navigate(TrackdemicsScreens.StudentHomeScreen.name)
                        "PROFESSOR" -> navController.navigate(TrackdemicsScreens.ProfessorHomeScreen.name)
                        "ADMIN" -> navController.navigate(TrackdemicsScreens.AdminHomeScreen.name)
                    }
                },
                onFailure = { error ->
                    loading = false
                    viewModel.clearSignUpState()

                    Log.d("SIGNUP_FAIL", "Sign up error: ${error.message}")
                    Toast.makeText(context, error.message ?: "Sign up failed", Toast.LENGTH_LONG)
                        .show()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                onBackClick = {
                    navController.navigate(TrackdemicsScreens.RoleScreen.name)
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
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
                            .padding(start = 24.dp, end = 16.dp, top = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        WelcomeText(
                            greet = "Welcome",
                            role = role
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

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
                            Spacer(modifier = Modifier.height(20.dp))

                            // Signup form
                            SignUpForm(
                                role = role,
                                loading = loading,
                                onSubmit = { email, password, firstName, lastName ->
                                    loading = true
                                    viewModel.signUp(email, password, role, firstName, lastName)
                                },
                                navController = navController
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 2.dp, end = 24.dp), // adjust bottom padding to match your design
                                contentAlignment = Alignment.BottomEnd
                            )
                            {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                )
                                {
                                    Text(
                                        text = "Already Have an Account?",
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Text(
                                        text = "Log IN",
                                        modifier = Modifier
                                            .padding(top = 2.dp)
                                            .clickable {
                                                navController.navigate(TrackdemicsScreens.LoginScreen.name + "/$role")
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


