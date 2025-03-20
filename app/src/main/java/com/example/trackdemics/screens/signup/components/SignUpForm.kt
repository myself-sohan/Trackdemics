package com.example.trackdemics.screens.signup.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.trackdemics.widgets.EmailInput
import com.example.trackdemics.widgets.PasswordInput
import com.example.trackdemics.widgets.SubmitButton

@Composable
fun SignUpForm(
    role: String,
    loading: Boolean,
    onClicked: (String) -> Unit)
{
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
    val valid = remember(email.value, password.value)
    {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier.Companion
        .wrapContentHeight()
        .background(Color.Transparent)
        .verticalScroll(rememberScrollState())
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        NameInput(
            modifier = Modifier,
            nameState = firstName,
            labelId = "First Name",
            enabled = !loading,
            onAction = KeyboardActions {
                lastNameFocusRequest.requestFocus()
            },
        )
        NameInput(
            nameState = lastName,
            modifier = Modifier
                .focusRequester(lastNameFocusRequest),
            labelId = "Last Name",
            enabled = !loading,
            onAction = androidx.compose.foundation.text.KeyboardActions {
                emailFocusRequest.requestFocus()
            },
        )
        EmailInput(
            modifier = Modifier
                .focusRequester(emailFocusRequest),
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
        )
        PasswordInput(
            modifier = Modifier.Companion
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
            textId = "SignUp",
            loading = loading,
            validInputs = valid
        )
        {
            onClicked(role)
            keyboardController?.hide()
        }
    }
}