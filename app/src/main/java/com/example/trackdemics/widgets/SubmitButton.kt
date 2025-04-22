package com.example.trackdemics.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit = {}
)
{
    Button(
        onClick = onClick,
        modifier = Modifier.Companion
            .padding(10.dp)
            .fillMaxWidth(0.5f),
        enabled = !loading && validInputs,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(0.5f),
            contentColor = MaterialTheme.colorScheme.onTertiary.copy(0.9f),
            containerColor = MaterialTheme.colorScheme.tertiary.copy(0.9f),
        )
    )
    {
        if (loading)
            CircularProgressIndicator(
                modifier = Modifier.Companion
                    .size(25.dp)
            )
        else
            Text(
                text = textId,
                modifier = Modifier.Companion
                    .padding(5.dp)
            )
    }
}