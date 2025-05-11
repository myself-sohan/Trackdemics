package com.example.trackdemics.screens.attendance.components

import android.app.DatePickerDialog
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R
import java.text.SimpleDateFormat
import java.util.*



@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year: Int, month: Int, dayOfMonth: Int ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                onDateSelected(sdf.format(cal.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = "📅 Date!",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Pick date",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                //fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                color = MaterialTheme.colorScheme.onPrimary
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            interactionSource = interactionSource
        )
    }

