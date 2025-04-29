package com.example.trackdemics.screens.home.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.utils.generateQrCodeBitmap

@Composable
fun QrCodeDialog(
    course: ProfessorCourse,
    onDismiss: () -> Unit
) {
    val qrContent = "${course.courseCode}_${System.currentTimeMillis()}" // Unique per generation
    val qrBitmap: Bitmap = generateQrCodeBitmap(qrContent)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "QR Code for ${course.courseName}")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "Generated QR Code",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
