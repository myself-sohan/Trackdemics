package com.example.trackdemics.screens.attendance.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trackdemics.R

@Composable
fun AttendanceTrendGraph(
    attendanceData: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    if (attendanceData.isEmpty()) return

    val maxAttendance = attendanceData.maxOfOrNull { it.second } ?: 100
    val minAttendance = attendanceData.minOfOrNull { it.second } ?: 0
    val attendanceRange = maxAttendance - minAttendance

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.inversePrimary,
            MaterialTheme.colorScheme.primary
        )
    )

    val axisTextColor = MaterialTheme.colorScheme.surface
    val isDarkTheme = isSystemInDarkTheme()
    val circleColor = if (isDarkTheme) Color(0xFF0D47A1) else Color.White

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    )
    {
        Text(
            text = "📊 Weekly Attendance Trend",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(start = 16.dp)
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(16.dp),
                )
        )
        {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(
                        top = 20.dp,
                        start = 15.dp,
                        bottom = 20.dp,
                        end = 60.dp
                    )
            ) {
                val width = size.width
                val height = size.height
                val xStep = width / (attendanceData.size - 1).coerceAtLeast(1)
                val verticalPadding = 16.dp.toPx()
                val yStep = (height - 2 * verticalPadding) / attendanceRange.coerceAtLeast(1)

                val graphOffsetX = 102f

                val path = Path().apply {
                    attendanceData.forEachIndexed { index, (_, percentPresent) ->
                        val x = index * xStep + graphOffsetX
                        val y = height - verticalPadding - (percentPresent - minAttendance) * yStep
                        if (index == 0) moveTo(x, y)
                        else cubicTo(
                            x - xStep / 2,
                            getY(
                                attendanceData,
                                index - 1,
                                minAttendance,
                                yStep,
                                height,
                                verticalPadding
                            ),
                            x - xStep / 2,
                            y,
                            x,
                            y
                        )
                    }
                }

                drawPath(
                    path = path,
                    brush = gradientBrush,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )

                attendanceData.forEachIndexed { index, (_, percentPresent) ->
                    val x = index * xStep + graphOffsetX
                    val y = height - verticalPadding - (percentPresent - minAttendance) * yStep
                    drawCircle(
                        color = circleColor,
                        radius = 6.dp.toPx(),
                        center = Offset(x, y),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                attendanceData.forEachIndexed { index, (label, _) ->
                    val x = index * xStep + graphOffsetX
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        x,
                        height + 20f,
                        Paint().apply {
                            textSize = 32f
                            color = axisTextColor.toArgb()
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }

                for (i in 0..4) {
                    val value = minAttendance + i * (attendanceRange / 4f)
                    val y = height - verticalPadding - (value - minAttendance) * yStep
                    drawContext.canvas.nativeCanvas.drawText(
                        "${value.toInt()}%",
                        0f,
                        y,
                        Paint().apply {
                            textSize = 28f
                            color = axisTextColor.toArgb()
                            textAlign = Paint.Align.LEFT
                        }
                    )
                }
            }
        }
    }
}


private fun getY(
    attendanceData: List<Pair<String, Int>>,
    index: Int,
    minAttendance: Int,
    yStep: Float,
    height: Float,
    verticalPadding: Float
): Float {
    return height - verticalPadding - (attendanceData[index].second - minAttendance) * yStep
}

@Composable
fun EmptyAttendanceTrend(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Attendance Trend Yet",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Once you've marked attendance for 5 sessions,\nwe'll show a trend graph here.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.img_graph2),
            contentDescription = "Empty Attendance Graph",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(16.dp)
                .size(240.dp)
        )
    }
}
