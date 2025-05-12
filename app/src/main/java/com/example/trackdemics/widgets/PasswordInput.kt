package com.example.trackdemics.widgets

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Companion.Done,
    onAction: KeyboardActions = KeyboardActions.Companion.Default
)
{
    val visualTransformation =
        if(passwordVisibility.value)
            VisualTransformation.Companion.None
        else
            PasswordVisualTransformation()
    InputField(
        modifier = modifier,
        valueState = passwordState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Companion.Password,
        imeAction = imeAction,
        onAction = onAction,
        visualTransformation = visualTransformation,
        passwordVisibility = passwordVisibility,
        iconShow = true
    )
}