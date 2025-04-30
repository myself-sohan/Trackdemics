package com.example.trackdemics.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import com.example.trackdemics.utils.generateQrCodeBitmap

@Composable
fun QrCodeDialog(content: String, onDismiss: () -> Unit) {
    val bitmap = generateQrCodeBitmap(content)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Scan to mark attendance!") },
        text = {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "QR Code"
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

