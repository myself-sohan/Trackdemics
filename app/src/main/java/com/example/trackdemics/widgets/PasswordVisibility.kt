package com.example.trackdemics.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visibility = passwordVisibility.value
    IconButton(onClick = {
        passwordVisibility.value = !visibility
    })
    {
        val imageVector = if (visibility) Icons.Default.VisibilityOff else Icons.Default.Visibility
        val description = if (visibility) "Hide password" else "Show password"
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            modifier = Modifier.size(25.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}