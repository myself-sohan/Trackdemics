package com.example.trackdemics.screens.attendance.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import com.example.trackdemics.ui.theme.onBackgroundLight
import com.example.trackdemics.ui.theme.surfaceContainerLowLight

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SemesterSelector(
    selectedSemester: String,
    semesterOptions: List<String>,
    onSemesterSelected: (String) -> Unit = {}
) {
    var expanded = remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(45.dp)
                .clickable { expanded.value = true },
            shape = RoundedCornerShape(35.dp),
            elevation = CardDefaults.cardElevation(14.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceContainerLowLight)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = selectedSemester,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = onBackgroundLight,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily(Font(R.font.tai_heritage_pro_regular))
                    )
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = onBackgroundLight
                )
            }
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        // Dropdown aligned below center of card
        Column(
            modifier = Modifier.fillMaxWidth(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                shape = RoundedCornerShape(15.dp),
                shadowElevation = 10.dp,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    //.align(Alignment.Top) // ✅ aligns center below the card
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
                    .padding(top = 10.dp)
            )
            {
                semesterOptions.forEach { semester ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            )
                            {
                                Text(
                                    text = semester,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        },
                        onClick = {
                            onSemesterSelected(semester)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}
