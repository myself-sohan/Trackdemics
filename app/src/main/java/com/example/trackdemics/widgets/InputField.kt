package com.example.trackdemics.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R

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
    Card(
        elevation = CardDefaults.cardElevation(35.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.9f)
        )
    ) {
        OutlinedTextField(
            value = valueState.value,
            onValueChange = {
                valueState.value = it
            },
            label = {
                Text(
                    text = labelId,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                fontFamily = FontFamily(Font(R.font.notosans_variablefont))
            ),
            singleLine = isSingleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                focusedLabelColor = MaterialTheme.colorScheme.primary.copy(1f),
                unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(1f),
                cursorColor = Color.Blue
            ),
            modifier = modifier
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                .height(70.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(0.7f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(0.9f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(0.7f),
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
            },
        )
    }
}