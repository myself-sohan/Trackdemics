package com.example.trackdemics.screens.signup

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                MaterialTheme.colorScheme.primary.copy(0.7f),
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
//                    //WelcomeText(
//                        greet = "Get Started",
//                        role = role
//                    )

                    SignUpForm(
                        role = role,
                        loading = loading,
                        onSubmit = { email, password, firstName, lastName ->
                            loading = true
                            viewModel.signUp(email, password, role, firstName, lastName)
                        },
                        navController = navController
                    )


                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Already Have an Account?")
                        Text(
                            text = "Log In",
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(TrackdemicsScreens.LoginScreen.name + "/$role")
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


