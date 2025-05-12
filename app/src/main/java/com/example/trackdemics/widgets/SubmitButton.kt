package com.example.trackdemics.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit = {}
)
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF3A5BFF),
                        Color(0xFF0F2B8E)
                    )
                ),
                shape = RoundedCornerShape(50)
            )
            .clickable(enabled = !loading && validInputs){
                // This is handled inside LoginForm normally, so maybe unused
                onClick()
            },
        contentAlignment = Alignment.Center
    )
    {
        if (loading)
            CircularProgressIndicator(
                modifier = Modifier.Companion
                    .size(25.dp)
            )
        else
        {
            Text(
                text = "LOG IN",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}