package com.example.trackdemics.screens.attendance.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import com.example.trackdemics.ui.theme.onBackgroundLight
import com.example.trackdemics.ui.theme.surfaceContainerLowLight

@SuppressLint("ReturnFromAwaitPointerEventScope")
@Composable
fun AddCourseCard(
    label: String = "Add Course",
    selectedOption: String?,
    modifier: Modifier = Modifier
        .height(55.dp)
        .fillMaxWidth(0.45f),
    imageVector: ImageVector = Icons.Default.Add,
    onClick:  () -> Unit = {},
)
{
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f, // shrink a bit on tap
        label = "ButtonScale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Remove extra default ripple
                onClick = onClick
            )
            .then(
                Modifier.pointerInput(interactionSource) {
                    while (true) {
                        val event = awaitPointerEventScope { awaitPointerEvent() }
                        isPressed = event.type == androidx.compose.ui.input.pointer.PointerEventType.Press
                    }
                }
            ),
        shape = RoundedCornerShape(35.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 18.dp else 14.dp // subtle lift on tap
        ),
        colors = CardDefaults.cardColors(containerColor = surfaceContainerLowLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedOption?: label,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = onBackgroundLight,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily(Font(R.font.tai_heritage_pro_regular))
                )
            )

            Icon(
                imageVector = imageVector,
                contentDescription = selectedOption?: label,
                tint = onBackgroundLight
            )
        }
    }
}
