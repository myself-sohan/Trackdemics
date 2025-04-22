package com.example.trackdemics.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    modifier: Modifier = Modifier.Companion,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Companion.Text,
    imeAction: ImeAction = ImeAction.Companion.Next,
    onAction: KeyboardActions = KeyboardActions.Companion.Default,
    visualTransformation: VisualTransformation = VisualTransformation.Companion.None,
    iconShow: Boolean = false,
    passwordVisibility: MutableState<Boolean> = mutableStateOf(false)
)
{
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
        },
        label = {
            Text(text = labelId)
        },
        textStyle = TextStyle(
            color = Color(0, 0, 102),
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        ),
        singleLine = isSingleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue.copy(0.5f),
            unfocusedBorderColor = Color.Blue.copy(0.5f),
            focusedLabelColor = Color.Blue.copy(0.5f),
            unfocusedLabelColor = Color.Blue.copy(0.5f),
            cursorColor = Color.Blue
        ),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(0.1f),
                    MaterialTheme.colorScheme.primary.copy(0.2f),
                    MaterialTheme.colorScheme.primary.copy(0.1f),
                )
            )
        ),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction,
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (iconShow)
                PasswordVisibility(passwordVisibility = passwordVisibility)
        }
    )
}