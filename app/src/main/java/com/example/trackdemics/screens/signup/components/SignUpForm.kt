package com.example.trackdemics.screens.signup.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.widgets.EmailInput
import com.example.trackdemics.widgets.PasswordInput
import com.example.trackdemics.widgets.SubmitButton

@Composable
fun SignUpForm(
    role: String,
    loading: Boolean,
    onSubmit: (String, String, String, String) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val lastNameFocusRequest = remember { FocusRequester() }
    val emailFocusRequest = remember { FocusRequester() }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .wrapContentHeight()
        .background(Color.Transparent)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NameInput(
            modifier = Modifier
                .height(70.dp),
            nameState = firstName,
            labelId = "First Name",
            enabled = !loading,
            onAction = KeyboardActions {
                lastNameFocusRequest.requestFocus()
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        NameInput(
            nameState = lastName,
            modifier = Modifier
                .focusRequester(lastNameFocusRequest)
                .height(70.dp),
            labelId = "Last Name",
            enabled = !loading,
            onAction = KeyboardActions {
                emailFocusRequest.requestFocus()
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        EmailInput(
            modifier = Modifier
                .focusRequester(emailFocusRequest)
                .height(70.dp),
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        PasswordInput(
            modifier = Modifier
                .focusRequester(passwordFocusRequest)
                .height(70.dp),
            passwordState = password,
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) {
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    return@KeyboardActions
                }
                keyboardController?.hide()
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        SubmitButton(
            textId = "Sign Up",
            loading = loading,
            validInputs = valid
        ) {
            if (!valid) {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                return@SubmitButton
            }
            onSubmit(
                email.value.trim(),
                password.value.trim(),
                firstName.value.trim(),
                lastName.value.trim()
            )
            keyboardController?.hide()
        }
    }
}
