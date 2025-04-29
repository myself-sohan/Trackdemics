package com.example.trackdemics.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.screens.attendance.model.StudentCourse
import com.example.trackdemics.ui.theme.onSurfaceLight

@Composable
fun StudentAttendanceCard(
    course: StudentCourse,
    navController: NavController
)
{
    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White)
    )
    {
        Box(
            modifier = Modifier.Companion
                .background(
                    Brush.Companion.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.colorScheme.outlineVariant,
                            //MaterialTheme.colorScheme.outline,
                        )
                    )
                )
        )
        {
            Column(modifier = Modifier.Companion.padding(16.dp)) {
                Text(
                    text = course.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color.Companion.White
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

                Text(
                    text = course.code,
                    fontSize = 19.sp,
                    color = Color.Companion.White
                )

                Spacer(modifier = Modifier.Companion.height(12.dp))

                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Attended",
                        fontSize = 14.sp,
                        color = onSurfaceLight.copy(alpha = 0.6f)
                    )

                    Text(
                        text = "${course.attended}/${course.total}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = Color.Companion.White
                    )
                }
            }
        }
    }
}