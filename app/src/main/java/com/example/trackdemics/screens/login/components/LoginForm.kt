package com.example.trackdemics.screens.login.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.example.trackdemics.widgets.EmailInput
import com.example.trackdemics.widgets.PasswordInput
import com.example.trackdemics.widgets.SubmitButton

@Composable
fun LoginForm(
    role: String,
    onClicked: (String) -> Unit={}
)
{
    val context = LocalContext.current
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var loading = false
    val valid = remember(email.value, password.value)
    {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier
        .height(280.dp)
        .background(Color.Transparent)
        .verticalScroll(rememberScrollState())
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
        )
        PasswordInput(
            modifier = Modifier
                .focusRequester(passwordFocusRequest),
            passwordState = password,
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) {
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    return@KeyboardActions
                }
            }
        )
        SubmitButton(
            textId = "Login",
            loading = loading,
            validInputs = valid
        )
        {
            onClicked(role)
            keyboardController?.hide()
        }
    }
}