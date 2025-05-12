package com.example.trackdemics.screens.login.components

import android.widget.Toast
import com.example.trackdemics.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.widgets.EmailInput
import com.example.trackdemics.widgets.PasswordInput
import com.example.trackdemics.widgets.SubmitButton

@Composable
fun LoginForm(
    role: String,
    loading: Boolean,
    onSubmit: (String, String) -> Unit // ✅ Updated from (String) -> Unit
) {
    val context = LocalContext.current
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
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
    )
    {
        EmailInput(
            modifier = Modifier
                .height(70.dp),
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
        )
        Spacer(modifier = Modifier.height(30.dp))
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
                //onSubmit(email.value.trim(), password.value.trim()) // ✅ Trigger login
            }
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Forgot password text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        )
        {
            Text(
                text = "Forgot password?",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.lobster_regular))
                ),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(54.dp))

        SubmitButton(
            textId = "Log In",
            loading = loading,
            validInputs = valid
        ) {
            if (!valid) {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                return@SubmitButton
            }
            keyboardController?.hide()
            onSubmit(email.value.trim(), password.value.trim()) // ✅ Trigger login
        }
    }
}
