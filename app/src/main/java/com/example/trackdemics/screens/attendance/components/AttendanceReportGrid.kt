package com.example.trackdemics.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import com.example.trackdemics.screens.attendance.AttendanceEntry

@Composable
fun AttendanceReportGrid(
    attendanceState: SnapshotStateList<AttendanceEntry>,
    onToggleAttendance: (AttendanceEntry) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = Modifier.Companion
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(attendanceState.size) { index ->
            val entry = attendanceState[index]

//            val pulseAnim = rememberInfiniteTransition(label = "pulse")
//            val scale by pulseAnim.animateFloat(
//                initialValue = 0.9f,
//                targetValue = 1.1f,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(durationMillis = 800, easing = LinearEasing),
//                    repeatMode = RepeatMode.Reverse
//                ), label = "scale"
//            )

            Card(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onToggleAttendance(entry)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.Companion
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = entry.student.rollNumber,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.Companion.height(2.dp))
                    Text(
                        text = "85%",
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 22.sp,
                        modifier = Modifier.Companion
                            .padding(start = 3.dp),
                        fontFamily = FontFamily(Font(R.font.lobster_regular)),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.Companion.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    )
                    {
                        Box(
                            modifier = Modifier.Companion
                                .size(12.dp)
                                .background(
                                    if (entry.isPresent) Color(0xFF4CAF50) else Color(0xFFF44336),
                                    shape = CircleShape
                                )
                        )

                        Row(
                            verticalAlignment = Alignment.Companion.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        )
                        {
                            Text(
                                text = if (entry.isPresent) "Present" else "Absent",
                                color = if (entry.isPresent) Color(0xFF4CAF50) else Color(0xFFF44336),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Companion.Bold
                            )

                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Tap to toggle",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.Companion.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}