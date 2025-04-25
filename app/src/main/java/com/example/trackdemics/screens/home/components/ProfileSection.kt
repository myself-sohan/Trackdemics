package com.example.trackdemics.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.R

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    label :String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.Companion.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
        contentAlignment = Alignment.Companion.Center
    ) {
        Column(
            modifier = Modifier.Companion,
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier.Companion
                    .scale(.50f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
            WelcomeCard(
                label = label
            )
        }
    }
}
@Composable
fun WelcomeCard(
    modifier: Modifier = Modifier,
    label: String
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .padding(bottom = 32.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(8.dp)

    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Companion.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                ),
                modifier = Modifier.Companion
                    .padding(12.dp),
                textAlign = TextAlign.Companion.Center
            )
        }
    }
}