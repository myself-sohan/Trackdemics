package com.example.trackdemics.screens.attendance.components

import androidx. compose. ui. graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackdemics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ConfirmationDialog(
    courseCode: String? = "322",
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    rightButtonLabel: String = "Delete",
    leftButtonLabel: String = "Cancel",
    title: String = "Delete Course?",
    message1: String = "Are you sure you want to delete ",
    message2: String = "? You can add it again later if needed.",
    titleIcon: ImageVector = Icons.Default.Warning,
    rightButtonIcon: ImageVector = Icons.Default.Delete,
    rightButtonColor: Color = MaterialTheme.colorScheme.error
)
{
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    )
    {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        {
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion.padding(24.dp)
            )
            {
                Icon(
                    imageVector = titleIcon,
                    contentDescription = "Warning",
                    tint = rightButtonColor,
                    modifier = Modifier.Companion.size(48.dp)
                )
                Spacer(modifier = Modifier.Companion.height(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Companion.Bold
                )
                Spacer(modifier = Modifier.Companion.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append(message1)

                        // Highlighted Course Name
                        if(courseCode!= null)
                        {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Companion.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append("\"$courseCode\"")
                            }
                        }

                        append(message2)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                    ),
                    textAlign = TextAlign.Companion.Center,
                    modifier = Modifier.Companion.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.Companion.fillMaxWidth()
                )
                {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.Companion
                            .weight(0.9f)
                            .height(48.dp)
                    )
                    {
                        Text(
                            text = leftButtonLabel,
                            modifier = Modifier.Companion
                                .padding(top = 5.dp)
                        )
                    }
                    Button(
                        onClick = {
                            onConfirm()
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = rightButtonColor
                        ),
                        modifier = Modifier.Companion
                            .weight(1.1f)
                            .height(48.dp)
                    )
                    {
                        Icon(
                            imageVector = rightButtonIcon,
                            contentDescription = rightButtonLabel,
                            modifier = Modifier.Companion.size(18.dp)
                        )
                        Spacer(modifier = Modifier.Companion.width(8.dp))
                        Text(
                            text = rightButtonLabel,
                            modifier = Modifier.Companion
                                .padding(top = 5.dp)
                        )
                    }
                }
            }
        }
    }
}