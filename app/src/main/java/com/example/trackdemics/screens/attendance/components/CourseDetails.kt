package com.example.trackdemics.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trackdemics.R
import com.example.trackdemics.widgets.LottieFromAssets

@Composable
fun CourseDetails(
    modifier: Modifier = Modifier.Companion,
    code: String,
    totalClass: Int,
    totalStudents: Int,
)
{
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.Companion.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
        contentAlignment = Alignment.Companion.TopCenter
    )
    {
        Row(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Companion.CenterVertically
        )
        {
            StatCard(label = "Classes", value = totalClass.toString())

            Surface(
                modifier = Modifier.Companion
                    .padding(4.dp)
                    .clip(RoundedCornerShape(topEndPercent = 33, bottomStartPercent = 33)),
                //.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                shadowElevation = 16.dp
            )
            {
                Text(
                    text = code,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.surface,
                    ),
                    fontFamily = FontFamily(Font(R.font.abril_fatface_regular)),
                    modifier = Modifier.Companion.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }

            StatCard(label = "Students", value = totalStudents.toString())
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String
)
{
    Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.inversePrimary,
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
        modifier = Modifier.Companion
            .padding(2.dp)
            .width(100.dp)
            .height(140.dp)
    )
    {
        //Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Companion.Center
        )
        {
            LottieFromAssets(
                modifier = Modifier.Companion
                    .size(90.dp)
            )
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.Companion.padding(6.dp)
            )
            {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Companion.Bold
                    )
                )
                Spacer(modifier = Modifier.Companion.height(26.dp))
                Box(
                    contentAlignment = Alignment.Companion.TopCenter
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.Companion.Black,
                            fontWeight = FontWeight.Companion.ExtraBold
                        ),
                    )
                }
            }

        }
    }
}