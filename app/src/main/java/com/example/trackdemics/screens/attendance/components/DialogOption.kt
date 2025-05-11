package com.example.trackdemics.screens.attendance.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DialogOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 18.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconTint,
            modifier = Modifier.Companion.size(24.dp)
        )
        Spacer(modifier = Modifier.Companion.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
            modifier = Modifier.Companion
                .padding(top = 5.dp)
        )
    }
}